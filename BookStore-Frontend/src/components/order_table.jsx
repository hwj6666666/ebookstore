import { Table } from "antd";
import OrderItemList from "./order_item_list";
import { formatTime } from "../utils/time";

export default function OrderTable({ orders }) {
  const columns = [
    { title: "收货人", dataIndex: "receiver", key: "receiver" },
    { title: "联系方式", dataIndex: "tel", key: "tel" },
    { title: "收货地址", dataIndex: "address", key: "address" },
    {
      title: "下单时间",
      dataIndex: "createdAt",
      key: "createdAt",
      render: (time) => formatTime(time),
    },
    {
      title: "总价",
      dataIndex: "total",
      key: "total",
      render: (total) => (total / 100).toFixed(2),
    },
  ];

  return (
    <Table
      columns={columns}
      expandable={{
        expandedRowRender: (order) => (
          <OrderItemList orderItems={order.items} />
        ),
      }}
      dataSource={orders.map((order) => ({
        ...order,
        key: order.id,
      }))}
    />
  );
}
