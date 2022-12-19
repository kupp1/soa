docker compose up -d

timeout 10

curl -X POST http://localhost:5555/v2/service_discovery/consul^
   -u admin:admin^
   -H "Content-Type: application/json"^
   -d "{ ""address"": ""consul-flat-service"", ""port"": 8500, ""enabled"": true, ""retry_timeout"": 10 }"

curl -X PUT http://localhost:8500/v1/agent/service/register^
   -H "Content-Type: application/json"^
   -d "{   ""ID"": ""flat-service-1"",   ""Name"": ""flat-service"",   ""Address"": ""flat-service-1"",   ""Port"": 443,   ""Check"":  {       ""tls_skip_verify"": true,       ""http"": ""https://flat-service-1:443/api/v1/"",       ""interval"": ""10s""   } }"

curl -X PUT http://localhost:8500/v1/agent/service/register^
   -H "Content-Type: application/json"^
   -d "{   ""ID"": ""flat-service-2"",   ""Name"": ""flat-service"",   ""Address"": ""flat-service-2"",   ""Port"": 443,   ""Check"":  {       ""tls_skip_verify"": true,       ""http"": ""https://flat-service-2:443/api/v1/"",       ""interval"": ""10s""   } }"

pause