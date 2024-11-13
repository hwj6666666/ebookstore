import { Card, Col, Row, Select, Space, Tag, Input, message } from "antd";
import { PrivateLayout } from "../components/layout";
import BookList from "../components/book_list";
import { useEffect, useRef, useState } from "react";
import { useSearchParams } from "react-router-dom";
import {
  fetchAuthor,
  getAllBookTags,
  getAuthor,
  searchBooks,
} from "../service/book";
import { TagOutlined } from "@ant-design/icons";
import axios from "axios";

const { Search } = Input;

export default function HomePage() {
  const [books, setBooks] = useState([]);
  const [totalPage, setTotalPage] = useState(0);
  const [selectedTag, setSelectedTag] = useState("");
  const [tags, setTags] = useState([]);
  const [searchParams, setSearchParams] = useSearchParams();
  const keyword = searchParams.get("keyword") || "";
  const pageIndex =
    searchParams.get("pageIndex") != null
      ? Number.parseInt(searchParams.get("pageIndex"))
      : 0;
  const pageSize =
    searchParams.get("pageSize") != null
      ? Number.parseInt(searchParams.get("pageSize"))
      : 10;
  const searchRef = useRef(null);

  useEffect(() => {
    const getBooks = async () => {
      let pagedBooks = await searchBooks(
        selectedTag,
        keyword,
        pageIndex,
        pageSize
      );
      let books = pagedBooks.items;
      let totalPage = pagedBooks.total;
      setBooks(books);
      setTotalPage(totalPage);
    };
    getBooks();
  }, [selectedTag, keyword, pageIndex, pageSize]);

  useEffect(() => {
    getAllBookTags().then(setTags);
  }, []);

  const handleSearch = (keyword) => {
    setSearchParams({
      keyword,
      pageIndex: 0,
      pageSize: 10,
    });
  };

  const handlePageChange = (page) => {
    setSearchParams({ keyword, pageIndex: page - 1, pageSize });
  };

  const handleSelectTag = (tag) => {
    setSelectedTag(tag);
    setSearchParams({ keyword, pageIndex: 0, pageSize });
    searchRef.current?.focus();
  };

  const handleAuthorSearch = async (title) => {
    try {
      const response = await axios.get(
        `${process.env.REACT_APP_BASE_URL}/author`,
        {
          params: { title },
        }
      );
      const author = response.data;
      if (author) {
        message.success(`作者: ${author}`);
      } else {
        message.warning("未查询到");
      }
    } catch (error) {
      message.error("查询失败");
    }
  };

  return (
    <PrivateLayout>
      <Card className="card-container">
        <Space direction="vertical" size="large" style={{ width: "100%" }}>
          <Row justify={"center"} gutter={10}>
            <Col span={8}>
              <Search
                ref={searchRef}
                placeholder="输入关键字"
                onSearch={handleSearch}
                enterButton
                size="large"
              />
            </Col>
            <Col span={2}>
              <Select
                size="large"
                style={{ width: "100%" }}
                placeholder={
                  <Space>
                    <TagOutlined />
                    标签
                  </Space>
                }
                options={tags.map((tag) => ({
                  label: <Tag>{tag}</Tag>,
                  value: tag,
                }))}
                onChange={handleSelectTag}
              />
            </Col>
            <Col span={4}>
              <Search
                placeholder="输入书名"
                onSearch={handleAuthorSearch}
                enterButton="查询作者"
                size="large"
              />
            </Col>
          </Row>
          <BookList
            books={books}
            pageSize={pageSize}
            total={totalPage * pageSize}
            current={pageIndex + 1}
            onPageChange={handlePageChange}
          />
        </Space>
      </Card>
    </PrivateLayout>
  );
}
