# Start the SSL Test Container

To start the SSL test container, you can use the following command:

```bash
docker build -t my-secure-httpbin .
docker run -p 3002:8080 my-secure-httpbin
```