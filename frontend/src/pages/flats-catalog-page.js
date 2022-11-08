import FlatsTable from "../components/tables/flats-table";
import {Divider, Layout, Space} from "antd";
import CustomFooter from "../components/general/custom-footer";
import CustomHeader from "../components/general/custom-header";
import {DeleteFlatForm} from "../components/forms/delete-flat-form";
import {ModifyFlatForm} from "../components/forms/modify-flat-form";

const {Content} = Layout;

export default function FlatsCatalogPage() {
    return (
        <>
            <Layout style={{minHeight: "100vh"}}>
                <CustomHeader
                    selectedMenuItem={'catalog'}
                />
                <Content>
                    <FlatsTable
                        pageSize={5}
                    />
                    <Divider/>
                    <Space style={{
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        flexDirection: "column",
                    }}
                           size={0}
                    >
                        <Space direction="vertical" size="middle" style={{ display: 'flex' }}>
                            <ModifyFlatForm/>
                            <DeleteFlatForm/>
                        </Space>
                    </Space>

                </Content>
                <CustomFooter/>
            </Layout>
        </>
    )
}