import {Layout, Menu} from "antd";
import {TableOutlined, LockOutlined, CalculatorOutlined} from "@ant-design/icons";
import {Link} from "react-router-dom";

const {Header} = Layout;

export default function CustomHeader({selectedMenuItem}) {
    const menuItems = [
        {
            icon: null,
            label: (
                <Link to="/flat">
                    Flats Service
                </Link>
            ),
            key: 'catalog',
        },
        {
            icon: null,
            label: (
                <Link to="/agency">
                    Agency Service
                </Link>
            ),
            key: 'agency',
        }
    ];

    return(
        <>
            <Header>
                <Menu
                    theme="dark"
                    mode="horizontal"
                    items={menuItems}
                    selectedKeys={[selectedMenuItem]}
                />
            </Header>
        </>
    )
}