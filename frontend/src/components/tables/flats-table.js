import {useEffect, useState} from "react";
import {Button, Space, Table, Typography} from "antd";
import axios from "axios";
import {useSnackbar} from "notistack";
import {
    parseMapFilters,
    parseFilters,
    parseSorterToQueryParam
} from "../../utils/parsers/table/sort-and-filter-parser";
import {getColumnSearchProps} from "./column-search";
import {FLATS_API} from "../../utils/api-constancts"
import {ReloadOutlined} from "@ant-design/icons";
import {AddFlatForm} from "../forms/add-flat-form";
import {MinAreaModal} from "../fast-response/min-area-modal";
import {ModifyFlatForm} from "../forms/modify-flat-form";
import {DeleteFlatForm} from "../forms/delete-flat-form";

export default function FlatsTable({pageSize}) {
    const {enqueueSnackbar, closeSnackbar} = useSnackbar();

    const [sortQueryParams, setSortQueryParams] = useState([]);
    const [filterModel, setFilterModel] = useState(new Map());
    const [currentPage, setCurrentPage] = useState(1);
    const [data, setData] = useState([]);
    const [totalCount, setTotalCount] = useState(0);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        getData(1, pageSize);
    }, [])

    const getData = (page, sort, filter) => {
        const queryParams = new URLSearchParams();

        queryParams.append("page", page - 1);
        queryParams.append("pageSize", pageSize);
        if (sort?.length) {
            sort.forEach((element) => {
                queryParams.append(`sort[${element.field}]`, element.order)
            })
        }
        if (filter) {
            filter.forEach((element) => {
                    queryParams.append(`filter[${element.field}]`, element.clause)
                }
            )
        }

        setLoading(true)

        axios
            .get(FLATS_API, {params: queryParams})
            .then((response) => {
                setData(response.data.flats.map((element) => {
                    return {
                        ...element,
                        key: element.id
                    }
                }))
                setTotalCount(response.data.totalFlats)
                setLoading(false)
            })
            .catch((err) => {
                enqueueSnackbar(err.response.data.messages[0], {
                    autoHideDuration: 5000,
                    variant: "error"
                })
            });
    };

    const handleTableChange = (pagination, filters, sorter) => {
        let sortQueryParams = [];
        let filterParams = [];

        const sortersArray = Array.from(sorter)

        if (sorter.hasOwnProperty("column")) {
            sortQueryParams[0] = parseSorterToQueryParam(sorter)
        } else if (sortersArray.length > 0) {
            sortQueryParams = sortersArray.map((element) => {
                return parseSorterToQueryParam(element)
            })
        }

        if (filters) {
            filterParams = parseFilters(filters)
        }

        let newFilterModel = new Map()

        Object.keys(filters).forEach((key) => {
                if (key && filters[key]) {
                    newFilterModel.set(key, [String([filters[key][0]]), filters[key][1]])
                }
            }
        )

        setCurrentPage(pagination.current)
        setSortQueryParams(sortQueryParams)
        setFilterModel(newFilterModel)

        getData(pagination.current, sortQueryParams, filterParams);
    };

    const handleFilterChange = (dataIndex, filterType, filterValue) => {
        if (filterType && filterValue) {
            const filtrationChanged =
                filterModel?.get(dataIndex)
                && (
                    filterModel?.get(dataIndex)[0] !== filterType
                    || filterModel?.get(dataIndex)[1] !== filterValue
                )

            if (filtrationChanged) {
                filterModel.set(dataIndex, [filterType, filterValue])
                getData(currentPage, sortQueryParams, parseMapFilters(filterModel))
                setFilterModel(filterModel)
            }
        }
    }

    const handleRefresh = () => {
        getData(currentPage, sortQueryParams, parseMapFilters(filterModel))
    }


    const  handeDelete = () => {
        const filters = parseMapFilters(filterModel);
        const queryParams = new URLSearchParams();
        if (filters) {
            filters.forEach((element) => {
                    queryParams.append(`filter[${element.field}]`, element.clause)
                }
            )
        }

        axios
            .delete(FLATS_API, {params: queryParams})
            .then((response) => {
                enqueueSnackbar(`Successfully deleted ${response.data.length} flats`, {
                    autoHideDuration: 5000,
                    variant: "success"
                })
            })
            .catch((err) => {
                enqueueSnackbar(err.response.data.messages[0], {
                    autoHideDuration: 5000,
                    variant: "error"
                })
            });
    }

    return (
        <>
            <Typography> &nbsp; </Typography>
            <Space style={{
                display: 'flex',
                alignItems: 'left',
                justifyContent: 'left',
            }} size={"middle"}>
                <Button icon={<ReloadOutlined/>} onClick={handleRefresh} style={{}}>
                    Refresh
                </Button>
                <Button danger onClick={handeDelete}>
                    Delete All Selected
                </Button>

                <Typography> &nbsp; </Typography>

                <AddFlatForm reloadTable={() => {getData(currentPage, sortQueryParams, parseMapFilters(filterModel))}}/>
                <MinAreaModal/>
            </Space>
            <Typography> &nbsp; </Typography>
            <Table
                columns={[
                    {
                        title: 'Id',
                        dataIndex: 'id',
                        key: 'id',
                        sorter: {multiple: 3},
                        sortDirections: ['ascend', 'descend'],
                        ...getColumnSearchProps('id', handleFilterChange)
                    },
                    {
                        title: 'Name',
                        dataIndex: 'name',
                        key: 'name',
                        sorter: {multiple: 3},
                        sortDirections: ['ascend', 'descend'],
                        ...getColumnSearchProps('name', handleFilterChange)
                    },
                    {
                        title: 'Coordinates',
                        children: [
                            {
                                title: 'X',
                                dataIndex: ['coordinates', 'x'],
                                key: 'coordinateX',
                                sorter: {multiple: 3},
                                sortDirections: ['ascend', 'descend'],
                                ...getColumnSearchProps('coordinates.x', handleFilterChange)
                            },
                            {
                                title: 'Y',
                                dataIndex: ['coordinates', 'y'],
                                key: 'coordinateY',
                                sorter: {multiple: 3},
                                sortDirections: ['ascend', 'descend'],
                                ...getColumnSearchProps('coordinates.y', handleFilterChange)
                            },
                        ]
                    },
                    {
                        title: 'Creation date',
                        dataIndex: 'createDate',
                        key: 'createDate',
                        sorter: {multiple: 3},
                        sortDirections: ['ascend', 'descend'],
                        ...getColumnSearchProps('createDate', handleFilterChange)
                    },
                    {
                        title: 'Area',
                        dataIndex: 'area',
                        key: 'area',
                        sorter: {multiple: 3},
                        sortDirections: ['ascend', 'descend'],
                        ...getColumnSearchProps('area', handleFilterChange)
                    },
                    {
                        title: 'Number Of Rooms',
                        dataIndex: 'numberOfRooms',
                        key: 'numberOfRooms',
                        sorter: {multiple: 3},
                        sortDirections: ['ascend', 'descend'],
                        ...getColumnSearchProps('numberOfRooms', handleFilterChange)
                    },
                    {
                        title: 'Furnish',
                        dataIndex: 'furnish',
                        key: 'furnish',
                        sorter: {multiple: 3},
                        sortDirections: ['ascend', 'descend'],
                        ...getColumnSearchProps('furnish', handleFilterChange)
                    },
                    {
                        title: 'View',
                        dataIndex: 'view',
                        key: 'view',
                        sorter: {multiple: 4},
                        sortDirections: ['ascend', 'descend'],
                        ...getColumnSearchProps('view', handleFilterChange)
                    },
                    {
                        title: 'Transport',
                        dataIndex: 'transport',
                        key: 'transport',
                        sorter: {multiple: 3},
                        sortDirections: ['ascend', 'descend'],
                        ...getColumnSearchProps('transport', handleFilterChange)
                    },
                    {
                        title: 'House',
                        children: [
                            {
                                title: 'Name',
                                dataIndex: ['house', 'name'],
                                key: 'houseName',
                                sorter: {multiple: 3},
                                sortDirections: ['ascend', 'descend'],
                                ...getColumnSearchProps('house.name', handleFilterChange)
                            },
                            {
                                title: 'Year',
                                dataIndex: ['house', 'year'],
                                key: 'houseYear',
                                sorter: {multiple: 3},
                                sortDirections: ['ascend', 'descend'],
                                ...getColumnSearchProps('house.year', handleFilterChange)
                            },
                            {
                                title: 'Number of lifts',
                                dataIndex: ['house', 'numberOfLifts'],
                                key: 'houseNumberOfLifts',
                                sorter: {multiple: 3},
                                sortDirections: ['ascend', 'descend'],
                                ...getColumnSearchProps('house.numberOfLifts', handleFilterChange)
                            }
                        ]
                    },
                    {
                        title: 'Price',
                        dataIndex: 'price',
                        key: 'price',
                        sorter: {multiple: 3},
                        sortDirections: ['ascend', 'descend'],
                        ...getColumnSearchProps('price', handleFilterChange)
                    },
                    {
                        title: 'Balcony',
                        dataIndex: 'hasBalcony',
                        render: val => (val ? 'True' : "False"),
                        key: 'hasBalcony',
                        sorter: {multiple: 3},
                        sortDirections: ['ascend', 'descend'],
                        ...getColumnSearchProps('hasBalcony', handleFilterChange)
                    },
                    {
                        title: 'Time to subway',
                        children: [
                            {
                                title: 'On Transport',
                                dataIndex: ["timeToSubwayOnTransport"],
                                key: 'timeToSubwayOnTransport',
                                sorter: {multiple: 3},
                                sortDirections: ['ascend', 'descend'],
                                ...getColumnSearchProps('timeToSubwayOnTransport', handleFilterChange)
                            },
                            {
                                title: 'On Foot',
                                dataIndex: ["timeToSubwayOnFoot"],
                                key: 'timeToSubwayOnFoot',
                                sorter: {multiple: 3},
                                sortDirections: ['ascend', 'descend'],
                                ...getColumnSearchProps('timeToSubwayOnFoot', handleFilterChange)
                            }
                        ]
                    }
                ]}
                rowKey="key"
                dataSource={data}
                onChange={handleTableChange}
                loading={loading}
                bordered={true}
                pagination={{
                    total: totalCount,
                    pageSize: pageSize,
                    showTotal: (total, range) => `${range[0]}-${range[1]}/${total}`
                }}
                scroll={{ x: 400 }}
            />

            <Space direction="vertical" size="middle" style={{ display: 'flex' }} align={"center"}>
                <ModifyFlatForm reloadTable={() => {getData(currentPage, sortQueryParams, parseMapFilters(filterModel))}}/>
                <DeleteFlatForm reloadTable={() => {getData(currentPage, sortQueryParams, parseMapFilters(filterModel))}}/>
            </Space>
        </>
    );
}