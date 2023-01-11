import {useEffect, useState} from "react";
import {Button, Form, InputNumber, Select, Space, Table, Typography} from "antd";
import axios from "axios";
import {useSnackbar} from "notistack";
import {
    parseMapFilters,
    parseFilters,
    parseSorterToQueryParam
} from "../../utils/parsers/table/sort-and-filter-parser";
import {getColumnSearchProps} from "./column-search";
import {FLATS_API, ORDERED_TO_METRO, WITH_BALCONY_API} from "../../utils/api-constancts"
import {ReloadOutlined} from "@ant-design/icons";
import {AddFlatForm} from "../forms/add-flat-form";
import {MinAreaModal} from "../fast-response/min-area-modal";
import {validateIntegerGreaterThanZero} from "../forms/validators";
import {useForm} from "antd/es/form/Form";
import {ModifyFlatForm} from "../forms/modify-flat-form";
import {DeleteFlatForm} from "../forms/delete-flat-form";

export default function AgencyTable({pageSize}) {
    const {enqueueSnackbar, closeSnackbar} = useSnackbar();

    const [sortQueryParams, setSortQueryParams] = useState([]);
    const [filterModel, setFilterModel] = useState(new Map());
    const [currentPage, setCurrentPage] = useState(1);
    const [data, setData] = useState([]);
    const [totalCount, setTotalCount] = useState(0);
    const [loading, setLoading] = useState(true);

    const [url, setUrl] = useState("");

    const [balconyForm] = useForm();
    const [timeForm] = useForm();

    useEffect(() => {
        getData(1);
    }, [url]);

    const getData = (page) => {
        if (url === "") return
        const queryParams = new URLSearchParams();

        queryParams.append("page", page - 1);
        queryParams.append("pageSize", pageSize);

        setLoading(false)

        axios
            .get(url, {params: queryParams})
            .then((response) => {
                setData(() => {
                    if (Array.isArray(response.data.flats)) {
                        console.log(response.data.flats.map((element) => {
                            return {
                                ...element,
                                key: element.id
                            }
                        }))
                        return response.data.flats.map((element) => {
                            return {
                                ...element,
                                key: element.id
                            }
                        })
                    } else {
                        return [{...response.data,
                            key: response.data.id }]
                    }
                })
                setTotalCount(response.data.totalFlats || 1)
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

    const handleBalcony = (e) => {
        setUrl(`${WITH_BALCONY_API}/${e.cheapest}/${e.hasBalcony}`)
    }

    const  handleTime = (e) => {
        setUrl(`${ORDERED_TO_METRO}/${e.byTransport}/${e.order}`)
    }

    return (
        <>
            <Typography> &nbsp; </Typography>
            <Space direction="horizontal" size="middle" style={{ display: 'flex' }}>
                <Form
                    form={balconyForm}
                    onFinish={handleBalcony}
                    layout="inline"
                >
                    <Form.Item
                        label="Price order"
                        name="cheapest"
                        rules={[
                            {required: true, message: 'Please input price order!'}
                        ]}
                    >
                        <Select style={{ width: 130 }}>
                            <Select.Option value={true}>Cheapest first</Select.Option>
                            <Select.Option value={false}>Expensive first</Select.Option>
                        </Select>
                    </Form.Item>

                    <Form.Item
                        label="With Balcony"
                        name="hasBalcony"
                        rules={[
                            {required: true, message: 'Please input has balcony!'}
                        ]}
                    >
                        <Select style={{ width: 100 }}>
                            <Select.Option value={true}>True</Select.Option>
                            <Select.Option value={false}>False</Select.Option>
                        </Select>
                    </Form.Item>
                    <Form.Item>
                        <Button type="primary" onClick={balconyForm.submit} style={{width: 150}}>
                            Find with balcony
                        </Button>
                    </Form.Item>
                </Form>
                <Typography> &nbsp; </Typography>
                <Typography> &nbsp; </Typography>
                <Typography> &nbsp; </Typography>
                <Typography> &nbsp; </Typography>
                <Form
                    form={timeForm}
                    onFinish={handleTime}
                    layout="inline"
                >
                    <Form.Item
                        label="Way type"
                        name="byTransport"
                        rules={[
                            {required: true, message: 'Please input way type!'}
                        ]}
                    >
                        <Select style={{ width: 130 }}>
                            <Select.Option value={true}>By Transport</Select.Option>
                            <Select.Option value={false}>On Foot</Select.Option>
                        </Select>
                    </Form.Item>

                    <Form.Item
                        label="Order"
                        name="order"
                        rules={[
                            {required: true, message: 'Please input has balcony!'}
                        ]}
                    >
                        <Select style={{ width: 100 }}>
                            <Select.Option value={true}>Desc</Select.Option>
                            <Select.Option value={false}>Acs</Select.Option>
                        </Select>
                    </Form.Item>
                    <Form.Item>
                        <Button type="primary" onClick={timeForm.submit} >
                            Find ordered by time to metro
                        </Button>
                    </Form.Item>
                </Form>
            </Space>
            <Typography> &nbsp; </Typography>

            <Table
                columns={[
                    {
                        title: 'Id',
                        dataIndex: 'id',
                        key: 'id',
                        sorter: {multiple: 3},
                        // sortDirections: ['ascend', 'descend'],
                        // ...getColumnSearchProps('id', handleFilterChange)
                    },
                    {
                        title: 'Name',
                        dataIndex: 'name',
                        key: 'name',
                        sorter: {multiple: 3},
                        // sortDirections: ['ascend', 'descend'],
                        // ...getColumnSearchProps('name', handleFilterChange)
                    },
                    {
                        title: 'Coordinates',
                        children: [
                            {
                                title: 'X',
                                dataIndex: ['coordinates', 'x'],
                                key: 'coordinateX',
                                sorter: {multiple: 3},
                                // sortDirections: ['ascend', 'descend'],
                                // ...getColumnSearchProps('coordinates.x', handleFilterChange)
                            },
                            {
                                title: 'Y',
                                dataIndex: ['coordinates', 'y'],
                                key: 'coordinateY',
                                sorter: {multiple: 3},
                                // sortDirections: ['ascend', 'descend'],
                                // ...getColumnSearchProps('coordinates.y', handleFilterChange)
                            },
                        ]
                    },
                    {
                        title: 'Creation date',
                        dataIndex: 'createDate',
                        key: 'createDate',
                        sorter: {multiple: 3},
                        // sortDirections: ['ascend', 'descend'],
                        // ...getColumnSearchProps('createDate', handleFilterChange)
                    },
                    {
                        title: 'Area',
                        dataIndex: 'area',
                        key: 'area',
                        sorter: {multiple: 3},
                        // sortDirections: ['ascend', 'descend'],
                        // ...getColumnSearchProps('area', handleFilterChange)
                    },
                    {
                        title: 'Number Of Rooms',
                        dataIndex: 'numberOfRooms',
                        key: 'numberOfRooms',
                        sorter: {multiple: 3},
                        // sortDirections: ['ascend', 'descend'],
                        // ...getColumnSearchProps('numberOfRooms', handleFilterChange)
                    },
                    {
                        title: 'Furnish',
                        dataIndex: 'furnish',
                        key: 'Furnish',
                        sorter: {multiple: 3},
                        // sortDirections: ['ascend', 'descend'],
                        // ...getColumnSearchProps('furnish', handleFilterChange)
                    },
                    {
                        title: 'View',
                        dataIndex: 'view',
                        key: 'view',
                        sorter: {multiple: 4},
                        // sortDirections: ['ascend', 'descend'],
                        // ...getColumnSearchProps('view', handleFilterChange)
                    },
                    {
                        title: 'Transport',
                        dataIndex: 'transport',
                        key: 'transport',
                        sorter: {multiple: 3},
                        // sortDirections: ['ascend', 'descend'],
                        // ...getColumnSearchProps('transport', handleFilterChange)
                    },
                    {
                        title: 'House',
                        children: [
                            {
                                title: 'Name',
                                dataIndex: ['house', 'name'],
                                key: 'house.name',
                                sorter: {multiple: 3},
                                // sortDirections: ['ascend', 'descend'],
                                // ...getColumnSearchProps('house.name', handleFilterChange)
                            },
                            {
                                title: 'Year',
                                dataIndex: ['house', 'year'],
                                key: 'house.year',
                                sorter: {multiple: 3},
                                // sortDirections: ['ascend', 'descend'],
                                // ...getColumnSearchProps('house.year', handleFilterChange)
                            },
                            {
                                title: 'Number of lifts',
                                dataIndex: ['house', 'numberOfLifts'],
                                key: 'house.numberOfLifts',
                                sorter: {multiple: 3},
                                // sortDirections: ['ascend', 'descend'],
                                // ...getColumnSearchProps('house.numberOfLifts', handleFilterChange)
                            }
                        ]
                    },
                    {
                        title: 'Price',
                        dataIndex: 'price',
                        key: 'price',
                        sorter: {multiple: 3},
                        // sortDirections: ['ascend', 'descend'],
                        // ...getColumnSearchProps('price', handleFilterChange)
                    },
                    {
                        title: 'Balcony',
                        dataIndex: 'hasBalcony',
                        render: val => (val ? 'True' : "False"),
                        key: 'hasBalcony',
                        sorter: {multiple: 3},
                        // sortDirections: ['ascend', 'descend'],
                        // ...getColumnSearchProps('hasBalcony', handleFilterChange)
                    },
                    {
                        title: 'Time to subway',
                        children: [
                            {
                                title: 'On Transport',
                                dataIndex: ["timeToSubwayOnTransport"],
                                key: 'timeToSubwayOnTransport',
                                sorter: {multiple: 3},
                                // sortDirections: ['ascend', 'descend'],
                                // ...getColumnSearchProps('timeToSubwayOnTransport', handleFilterChange)
                            },
                            {
                                title: 'On Foot',
                                dataIndex: ["timeToSubwayOnFoot"],
                                key: 'timeToSubwayOnFoot',
                                sorter: {multiple: 3},
                                // sortDirections: ['ascend', 'descend'],
                                // ...getColumnSearchProps('timeToSubwayOnFoot', handleFilterChange)
                            }
                        ]
                    }
                ]}
                rowKey="key"
                dataSource={data}
                onChange={handleTableChange}
                loading={false}
                bordered={true}
                pagination={{
                    total: totalCount,
                    pageSize: pageSize,
                    showTotal: (total, range) => `${range[0]}-${range[1]}/${total}`
                }}
                scroll={{ x: 400 }}
            />
        </>
    );
}