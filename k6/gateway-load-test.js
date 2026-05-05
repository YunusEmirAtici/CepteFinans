import http from "k6/http";
import { check, sleep } from "k6";

export const options = {
  stages: [
    { duration: "10s", target: 10 },
    { duration: "30s", target: 50 },
    { duration: "10s", target: 0 },
  ],
  thresholds: {
    http_req_failed: ["rate<0.05"],
    http_req_duration: ["p(95)<1000"],
  },
};

const BASE_URL = __ENV.BASE_URL || "http://localhost:8080";
const API_KEY = __ENV.API_KEY || "dev-key";

export function setup() {
  const params = {
    headers: {
      "Content-Type": "application/json",
      "X-API-KEY": API_KEY,
    },
  };

  const userPayload = JSON.stringify({
    username: `loadtest_user_${Date.now()}`,
    email: `loadtest_${Date.now()}@test.com`,
  });
  const userRes = http.post(`${BASE_URL}/api/users`, userPayload, params);

  const productPayload = JSON.stringify({
    name: `Test Product ${Date.now()}`,
    price: 99.99,
  });
  const productRes = http.post(`${BASE_URL}/api/products`, productPayload, params);

  return { userId: userRes.json("id"), productId: productRes.json("id") };
}

export default function (data) {
  const params = {
    headers: {
      "Content-Type": "application/json",
      "X-API-KEY": API_KEY,
    },
  };

  // User Service
  const userRes = http.get(`${BASE_URL}/api/users`, params);
  check(userRes, {
    "GET /api/users status is 200": (r) => r.status === 200,
  });

  const budgetPayload = JSON.stringify({
    userId: "loadtest_user",
    category: "Food",
    limitAmount: 1000,
    spentAmount: 500,
    month: 5,
    year: 2026,
  });
  const budgetRes = http.post(`${BASE_URL}/api/budgets`, budgetPayload, params);
  check(budgetRes, {
    "POST /api/budgets status is 201": (r) => r.status === 201,
  });

  const notificationPayload = JSON.stringify({
    userId: "loadtest_user",
    type: "INFO",
    message: "Load test notification",
  });
  const notifRes = http.post(`${BASE_URL}/api/notifications`, notificationPayload, params);
  check(notifRes, {
    "POST /api/notifications status is 201": (r) => r.status === 201,
  });

  const reportPayload = JSON.stringify({
    userId: "loadtest_user",
    type: "MONTHLY_SUMMARY",
    startDate: "2026-05-01",
    endDate: "2026-05-31",
  });
  const reportRes = http.post(`${BASE_URL}/api/reports`, reportPayload, params);
  check(reportRes, {
    "POST /api/reports status is 201": (r) => r.status === 201,
  });

  // Transaction Service
  const txPayload = JSON.stringify({
    userId: "loadtest_user",
    amount: 50.00,
    type: "EXPENSE",
    category: "Food",
    transactionDate: "2026-05-03",
    description: "Load test expense",
    recurring: false,
  });
  const txRes = http.post(`${BASE_URL}/api/transactions`, txPayload, params);
  check(txRes, {
    "POST /api/transactions status is 201": (r) => r.status === 201,
  });

  // Subscription Service
  const subPayload = JSON.stringify({
    userId: "loadtest_user",
    name: "Netflix",
    amount: 99.90,
    billingCycle: "MONTHLY",
    nextBillingDate: "2026-06-01",
    active: true,
  });
  const subRes = http.post(`${BASE_URL}/api/subscriptions`, subPayload, params);
  check(subRes, {
    "POST /api/subscriptions status is 201": (r) => r.status === 201,
  });

  // Health checks
  const healthEndpoints = [
    "/api/budgets/health",
    "/api/notifications/health",
    "/api/reports/health",
  ];
  healthEndpoints.forEach((endpoint) => {
    const res = http.get(`${BASE_URL}${endpoint}`, params);
    check(res, {
      [`${endpoint} status is 200`]: (r) => r.status === 200,
    });
  });

  sleep(1);
}
