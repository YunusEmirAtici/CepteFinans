import http from "k6/http";
import { check, sleep } from "k6";

export const options = {
  vus: 20,
  duration: "30s",
  thresholds: {
    http_req_failed: ["rate<0.05"],
    http_req_duration: ["p(95)<800"],
  },
};

const BASE_URL = __ENV.BASE_URL || "http://localhost:8080";
const API_KEY = __ENV.API_KEY || "dev-key";

export default function () {
  const params = {
    headers: {
      "X-API-KEY": API_KEY,
    },
  };
  const userRes = http.get(`${BASE_URL}/api/users`, params);
  check(userRes, {
    "users status is 200 or 401": (r) => r.status === 200 || r.status === 401,
  });

  const productRes = http.get(`${BASE_URL}/api/products`, params);
  check(productRes, {
    "products status is 200 or 401": (r) => r.status === 200 || r.status === 401,
  });

  sleep(1);
}
