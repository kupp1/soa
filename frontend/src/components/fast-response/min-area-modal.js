import {SimpleResponseModel} from "./general/simple-response-modal";
import {Button} from "antd";
import {useState} from "react";
import axios from "axios";
import {MIN_AREA_API} from "../../utils/api-constancts";
import {parseError} from "../../utils/parsers/xml/error-parser";
import {useSnackbar} from "notistack";
import {parseHeightSumResponse} from "../../utils/parsers/xml/simple-requests-parser";

export function MinAreaModal() {
    const {enqueueSnackbar, closeSnackbar} = useSnackbar();

    const [modalVisible, setModalVisible] = useState(false);
    const [modalValue, setModalValue] = useState();

    const handelOpen = () => {
        axios.get(MIN_AREA_API)
            .then((response) => {
                setModalValue(response.data.id)
                setModalVisible(true);
            })
            .catch((err) => {
                enqueueSnackbar(err.response.data.messages[0], {
                    autoHideDuration: 5000,
                    variant: "error"
                })
            });
    }

    const handelModalOk = () =>{
        setModalVisible(false)
    }

    return (
        <>
            <Button type="primary" onClick={handelOpen}>
                Get flat with min area
            </Button>
            <SimpleResponseModel
                title="Get flat with min area"
                visible={modalVisible}
                value={modalValue}
                handleOk={handelModalOk}
            />
        </>
    )
}