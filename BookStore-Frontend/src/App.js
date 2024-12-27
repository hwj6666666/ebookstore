import { ConfigProvider, theme } from "antd";
import AppRouter from "./components/router";
import { ApolloClient, InMemoryCache, ApolloProvider } from "@apollo/client";

const client = new ApolloClient({
  uri: "http://localhost:8070/graphql",
  cache: new InMemoryCache(),
});

function App() {
  const themeToken = {
    colorPrimary: "#1DA57A",
    colorInfo: "#1DA57A",
  };

  return (
    <ApolloProvider client={client}>
      <ConfigProvider
        theme={{
          algorithm: theme.defaultAlgorithm,
          token: themeToken,
        }}
      >
        <AppRouter />
      </ConfigProvider>
    </ApolloProvider>
  );
}

export default App;
