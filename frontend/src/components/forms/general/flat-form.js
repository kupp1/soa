import {Checkbox, Form, Input, InputNumber, Modal, Radio, Select} from "antd";
import {useForm} from "antd/es/form/Form";
import {useEffect} from "react";

export default function FlatForm({formVisible, onCancel, onFinish, title, initialValues}) {
    const [form] = useForm();

    useEffect(() => {
        if (initialValues) {
            form.setFieldsValue(initialValues)
        }
    }, [initialValues])

    return (
        <>
            <Modal
                title={title}
                open={formVisible}
                onOk={form.submit}
                onCancel={onCancel}
                width={1000}
            >
                <Form
                    form={form}
                    onFinish={onFinish}
                    labelCol={{span: 4}}
                    wrapperCol={{span: 8}}
                    layout="horizontal"
                >
                    <Form.Item
                        name="id"
                        hidden={true}
                    >
                        <Input/>
                    </Form.Item>

                    <Form.Item
                        label="Name"
                        name="name"
                        rules={[
                            {required: true, message: 'Please input name!'},
                        ]}
                    >
                        <Input/>
                    </Form.Item>
                    <Form.Item label="Coordinates">
                        <Input.Group>
                            <Form.Item
                                label="X"
                                name={["coordinates", "x"]}
                                rules={[
                                    {required: true, message: 'Please input X!'}
                                ]}
                            >
                                <InputNumber/>
                            </Form.Item>
                            <Form.Item
                                label="Y"
                                name={["coordinates", "y"]}
                                rules={[
                                    {required: true, message: 'Please input y!'},
                                ]}
                            >
                                <InputNumber/>
                            </Form.Item>
                        </Input.Group>
                    </Form.Item>
                    <Form.Item
                        label="Area"
                        name="area"
                        rules={[
                            {required: true, message: 'Please input area!'},
                            () => ({
                                validator(_, value) {
                                    if (value >= 0) {
                                        return Promise.resolve();
                                    }
                                    return Promise.reject(new Error('Area must be greater than 0!'));
                                },
                            }),
                        ]}
                    >
                        <InputNumber/>
                    </Form.Item>
                    <Form.Item
                        label="Number of rooms"
                        name="numberOfRooms"
                        rules={[
                            {required: true, message: 'Please input number of rooms!'},
                            () => ({
                                validator(_, value) {
                                    if (Number.isInteger(Number(value)) && value >= 0) {
                                        return Promise.resolve();
                                    }
                                    return Promise.reject(new Error('Number of rooms must be integer greater than 0!'));
                                },
                            }),
                        ]}
                    >
                        <InputNumber/>
                    </Form.Item>
                    <Form.Item
                        label="Furnish"
                        name="furnish"
                        rules={[
                            {required: true, message: 'Please input furnish!'}
                        ]}
                    >
                        <Select>
                            <Select.Option value="DESIGNER">DESIGNER</Select.Option>
                            <Select.Option value="FINE">FINE</Select.Option>
                            <Select.Option value="BAD">BAD</Select.Option>
                            <Select.Option value="LITTLE">LITTLE</Select.Option>
                        </Select>
                    </Form.Item>
                    <Form.Item
                        label="View"
                        name="view"
                        rules={[
                            {required: true, message: 'Please input view!'}
                        ]}
                    >
                        <Select>
                            <Select.Option value="STREET">STREET</Select.Option>
                            <Select.Option value="YARD">YARD</Select.Option>
                            <Select.Option value="PARK">PARK</Select.Option>
                            <Select.Option value="BAD">BAD</Select.Option>
                            <Select.Option value="GOOD">GOOD</Select.Option>
                        </Select>
                    </Form.Item>
                    <Form.Item
                        label="Transport"
                        name="transport"
                        rules={[
                            {required: true, message: 'Please input view!'}
                        ]}
                    >
                        <Select>
                            <Select.Option value="FEW">FEW</Select.Option>
                            <Select.Option value="NONE">NONE</Select.Option>
                            <Select.Option value="ENOUGH">ENOUGH</Select.Option>
                        </Select>
                    </Form.Item>
                    <Form.Item label="House">
                        <Input.Group>
                            <Form.Item
                                label="Name"
                                name={["house", "name"]}
                                rules={[
                                    {required: false, message: 'Please input name!'},
                                ]}
                            >
                                <Input/>
                            </Form.Item>
                            <Form.Item
                                label="Year"
                                name={["house", "year"]}
                                rules={[
                                    {required: true, message: 'Please input year!'},
                                    () => ({
                                        validator(_, value) {
                                            if (Number.isInteger(Number(value)) && value >= 0 && value <= 808) {
                                                return Promise.resolve();
                                            }
                                            return Promise.reject(new Error('Year must be in [1, 808]!'));
                                        },
                                    }),
                                ]}
                            >
                                <InputNumber/>
                            </Form.Item>
                            <Form.Item
                                label="Number of lifts"
                                name={["house", "numberOfLifts"]}
                                rules={[
                                    {required: true, message: 'Please input number of lifts!'},
                                    () => ({
                                        validator(_, value) {
                                            if (Number.isInteger(Number(value)) && value >= 0) {
                                                return Promise.resolve();
                                            }
                                            return Promise.reject(new Error('Number of lifts must be integer greater than 0!'));
                                        },
                                    }),
                                ]}
                            >
                                <InputNumber/>
                            </Form.Item>
                        </Input.Group>
                    </Form.Item>
                    <Form.Item
                        label="Price"
                        name="price"
                        rules={[
                            {required: true, message: 'Please input price!'},
                            () => ({
                                validator(_, value) {
                                    if (value >= 0) {
                                        return Promise.resolve();
                                    }
                                    return Promise.reject(new Error('Price must be greater than 0!'));
                                },
                            }),
                        ]}
                    >
                        <InputNumber/>
                    </Form.Item>
                    <Form.Item
                        label="Has Balcony"
                        name="hasBalcony"
                        rules={[
                            {required: true, message: 'Please input has balcony!'}
                        ]}
                    >
                        <Select>
                            <Select.Option value={true}>True</Select.Option>
                            <Select.Option value={false}>False</Select.Option>
                        </Select>
                    </Form.Item>
                    <Form.Item label="Time to subway">
                        <Input.Group>
                            <Form.Item
                                label="On Foot"
                                name={["timeToSubwayOnFoot"]}
                                rules={[
                                    {required: true, message: 'Please input time to subway on foot!'},
                                    () => ({
                                        validator(_, value) {
                                            if (Number.isInteger(Number(value)) && value >= 0) {
                                                return Promise.resolve();
                                            }
                                            return Promise.reject(new Error('Must be greater than 0!'));
                                        },
                                    }),
                                ]}
                            >
                                <Input/>
                            </Form.Item>
                            <Form.Item
                                label="On Transport"
                                name={["timeToSubwayOnTransport"]}
                                rules={[
                                    {required: true, message: 'Please input time to subway on transport!'},
                                    () => ({
                                        validator(_, value) {
                                            if (Number.isInteger(Number(value)) && value >= 0) {
                                                return Promise.resolve();
                                            }
                                            return Promise.reject(new Error('Must be greater than 0!'));
                                        },
                                    }),
                                ]}
                            >
                                <InputNumber/>
                            </Form.Item>
                        </Input.Group>
                    </Form.Item>
                </Form>
            </Modal>
        </>
    )
}