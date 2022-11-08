import {Divider, Layout, Space} from "antd";
import AgencyTable from "../components/tables/agency-table";
import CustomHeader from "../components/general/custom-header";
import FlatsTable from "../components/tables/flats-table";
import {ModifyFlatForm} from "../components/forms/modify-flat-form";
import {DeleteFlatForm} from "../components/forms/delete-flat-form";
import CustomFooter from "../components/general/custom-footer";
import {Content} from "antd/es/layout/layout";

export function AgencyPage() {
    return(
        <>
            <Layout style={{minHeight: "100vh"}}>
                <CustomHeader
                    selectedMenuItem={'agency'}
                />
                <Content>
                    <AgencyTable pageSize={5}/>
                    <Divider/>
                    <Space style={{
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        flexDirection: "column",
                    }}
                           size={0}
                    >
                    </Space>

                </Content>
                <CustomFooter/>
            </Layout>
        </>
    )
}