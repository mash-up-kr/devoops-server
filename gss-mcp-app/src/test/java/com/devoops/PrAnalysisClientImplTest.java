package com.devoops;

import com.devoops.client.PrAnalysisClientImpl;
import com.devoops.dto.request.AnalyzePrRequest;
import com.devoops.dto.response.PrAnalysis;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
//@Disabled
class PrAnalysisClientImplTest {

    @Autowired
    PrAnalysisClientImpl prAnalysisClient;

    @Test
    void analyzePr_shouldReturnSummaryAndQuestions() {
        // given
//        String title = "회원가입 시 이메일 중복 체크 로직 추가";
//        String desc = "회원가입 시 이메일 중복 체크 로직 추가입니다.";
//        String diff = """
//                diff --git a/UserService.java b/UserService.java
//                +    public boolean isEmailTaken(String email) {
//                +        return userRepository.existsByEmail(email);
//                +    }
//                +
//                +    public void register(User user) {
//                +        if (isEmailTaken(user.getEmail())) {
//                +            throw new DuplicateEmailException();
//                +        }
//                +        userRepository.save(user);
//                +    }
//                """;

        String title = "feat: MSW 세팅 및 일부 mock api 제작";
        String desc = """
                "<!--\\r\\n# For Maintainers\\r\\n\\r\\n- 최소한의 설명(팀에 속하지 않은 사람에게 PR을 이해하도록 설명하는 것을 목표로 합니다.)\\r\\n- 코드 변경사항의 논리를 PR 리뷰어에게 설명하기 위해 필요한 경우 해당 코드 라인에 Review Comment를 추가합니다.\\r\\n-->\\r\\n\\r\\n## What?\\r\\n\\r\\nclose #94 \\r\\n\\r\\n-  MSW(Mock Service Worker)에 대한 세팅을 진행하였습니다.\\r\\n- 모든 api에 대하여 작업을 해두기가 어려워, 테스트를 위해 홈에 들어가는 api를 우선적으로 작업해 두었습니다. \\r\\n(아래는 작업된 mock api 리스트 입니다.)\\r\\n\\r\\n|repositoriesHandler|userHandler|\\r\\n|:---:|:---:|\\r\\n|[/api/repositories/{repositoryId}/pull-requests](https://api.dev-oops.kr/swagger-ui/index.html#/Repository%20API/getRepositoryPullRequests)</br>[/api/repositories/me](https://api.dev-oops.kr/swagger-ui/index.html#/Repository%20API/getMyRepositories)</br>[/api/repositories/pull-requests](https://api.dev-oops.kr/swagger-ui/index.html#/Repository%20API/getRepositoryEntirePullRequests)</br>[/api/repositories/pull-requests/{pullRequestId}](https://api.dev-oops.kr/swagger-ui/index.html#/Pull%20Request%20API/getPullRequest)|[/api/users/me](https://api.dev-oops.kr/swagger-ui/index.html#/User%20API/getMyInfo)|\\r\\n\\r\\n- `.env.local`에 대한 변화가 있어, 노션에 추가해 두었습니다. ([링크](https://www.notion.so/Env-21bbee7f599680ca9d45fc980cf08baa?source=copy_link))\\r\\n\\r\\n## Why?\\r\\n\\r\\n- 백엔드가 먹통이 되니 프론트엔드 개발이 지연되는 문제를 방지하고 싶었습니다.\\r\\n- 또한 차후 백엔드 작업으로 부터 의존성을 제거할 수 있다고 생각해 진행하게 되었습니다.\\r\\n\\r\\n## How?\\r\\n\\r\\n<p><s>아.. 이번 일을 왜 시작했을까 싶을정도로 하면서 후회를...</s></p>\\r\\n\\r\\n서버가 멀쩡하게 돌아가는게 얼마나 감사한지 깨달았습니다.\\r\\n(MSW를 tanstack, Next app router과 함께 쓰려니 설정하는 부분에 있어서 수많은 문제들이 있었습니다..)\\r\\n\\r\\n### 📌 MSW에 대한 간단한 설명\\r\\n\\r\\nMSW는 크게 다음과 같은 역할을 한다고 생각하시면 됩니다.\\r\\n- 브라우저가 실제 서버로 보내려는 네트워크 요청을 가로챈다.\\r\\n- 개발자가 정의한 핸들러(handler)를 통해, 미리 설정해둔 값을 반환한다.\\r\\n\\r\\n### 📌 mocks 폴더 구조에 대한 설명\\r\\n\\r\\nmocks 폴더는 거의 MSW를 위해 만들어 졌다고 보시면 됩니다. \\r\\n(공식문서에서도 이렇게 작업하라고 되어 있어용.)\\r\\n\\r\\n```\\r\\nmocks/\\r\\n├── handlers/ <- API 요청(내 정보 조회 등)을 어떻게 처리할지 정의하는 폴더\\r\\n│   ├── repositoriesHandler.ts\\r\\n│   └── userHandler.ts\\r\\n├── responses/ <- mock response를 정의 하는 파일 (하위 폴더는 apis 처럼 endpoint를 기준으로 나눔)\\r\\n│   ├── repositories/\\r\\n│   │   ├── getEntirePullRequests.json\\r\\n│   │   ├── getPullRequest.json\\r\\n│   │   ├── getRepositoriesMe.json\\r\\n│   │   └── getRepositoryPullRequests.json\\r\\n│   ├── user/ \\r\\n│       └── getMyInfo.json\\r\\n├── browser.ts <- 브라우저에서 실행하기 위한 파일\\r\\n├── handlers.ts <- 위의 /handlers 폴더에서 정의한 파일을 다 묶은 파일\\r\\n├── index.ts <- 실행에 대한 로직이 들어있는 파일\\r\\n└── server.ts <- 서버에서 돌하가게 하기 위한 파일\\r\\n```\\r\\n\\r\\n### 📌 실행 위해 알아두어야 할 것\\r\\n\\r\\nMSW는 개발을 위한 것이므로 production 환경에서는 돌아갈 필요가 없습니다.\\r\\n그래서 `process.env.NODE_ENV !== \\"production\\"`으로 처리할까 하다가, 사용자가 원할때 MSW를 껏다 켰다 쉽게 하면 좋을 것 같아서 `.env.local`에 `NEXT_PUBLIC_MOCK_API` 값을 통해서 제어하는 방식으로 해 두었습니다. \\r\\n\\r\\n```\\r\\nNEXT_PUBLIC_MOCK_API=\\"enabled\\" // 실행 o\\r\\nNEXT_PUBLIC_MOCK_API=\\"disabled\\" // 실행 x\\r\\n```\\r\\n|<img width=\\"735\\" height=\\"258\\" alt=\\"스크린샷 2025-08-17 오후 8 21 53\\" src=\\"https://github.com/user-attachments/assets/0743aca3-a7cc-4e30-a10a-d07f54665a8c\\" />|\\r\\n|:---:|\\r\\n|정상적으로 실행되면, 다음과 같이 빨간색으로 실행되었다는 문구가 뜹니다.|\\r\\n\\r\\n- 추가1) msw 뜰때는 token과 상관없이 개발이 되어야 한다 생각해서 홈으로 접근해도 landing으로 redirect 되지 않도록 처리 했습니다\\r\\n- 추가2) `/api/repositories/${repositoryId}/pull-requests`과 같이 인자에 따라서 결과가 달라지는 api의 경우 인자의 값이 달라도 동일한 값이 전달되도록 해 두었습니다.\\r\\n\\r\\n### 📌 실행 화면\\r\\n\\r\\n|<img width=\\"1792\\" height=\\"1094\\" alt=\\"스크린샷 2025-08-17 오후 8 32 03\\" src=\\"https://github.com/user-attachments/assets/b9c43ff8-f1b0-4382-a8ee-0ccdbd288089\\" />|<img width=\\"1792\\" height=\\"1094\\" alt=\\"스크린샷 2025-08-17 오후 8 32 16\\" src=\\"https://github.com/user-attachments/assets/099eec32-40e3-4c36-ba7f-72b639020cb1\\" />|\\r\\n|:---:|:---:|\\r\\n|처음 실행하면 다음과 같이 뜰수도 있는데 새로고침 하시면 됩니다!|전체와 첫번째 레포의 경우 문제없이 동작|\\r\\n\\r\\n|<img width=\\"1792\\" height=\\"1094\\" alt=\\"스크린샷 2025-08-17 오후 8 32 25\\" src=\\"https://github.com/user-attachments/assets/5fd3f5f8-1e05-405f-ab19-4c0457db7913\\" />|\\r\\n|:---:|\\r\\n|두번째 레포의 경우 handler에서 404 에러가 발생하도록 해둠|\\r\\n\\r\\n## Check List\\r\\n\\r\\n- [x] Merge 할 브랜치가 올바른가?\\r\\n\\r\\n\\r\\n<!-- This is an auto-generated comment: release notes by coderabbit.ai -->\\n## Summary by CodeRabbit\\n\\n* **New Features**\\n  * Mock API 모드 도입(NEXT_PUBLIC_MOCK_API='enabled'): MSW 기반 클라이언트/서버 초기화와 자동 활성화로 사용자·레포/PR 응답 시뮬레이션 제공.\\n  * 여러 모의 핸들러와 JSON 응답 추가로 목록·상세·사용자 정보 뷰 테스트 가능.\\n\\n* **Chores**\\n  * msw 개발 의존성 및 루트 설정 추가, 서비스워커 스크립트 타입검사 제외.\\n  * 라우트 상수(ROUTES.API) 대거 추가·정리 및 RETROSPECTIVE 파라미터 필수화.\\n\\n* **Style**\\n  * ESLint 규칙 추가/경고화로 코드 스타일 강화.\\n\\n* **Behavior**\\n  * 미들웨어: Mock 모드일 때 보호 경로 토큰 검사 건너뜀.\\n\\n* **Other**\\n  * 애플리케이션 상수(페이지당 항목 수 등) 추가.\\n<!-- end of auto-generated comment: release notes by coderabbit.ai -->"
                """;
        String diff = example();
        long startTime = System.currentTimeMillis();
        AnalyzePrRequest request = new AnalyzePrRequest(title, desc, diff, "gpt-5-nano");
        PrAnalysis result = prAnalysisClient.analyze(request).prAnalysis();
        long endTime = System.currentTimeMillis();

        System.out.println(endTime - startTime+ "ms");

        System.out.println("📝 요약: " + result.summary());
        result.summaryDetails().forEach(q -> System.out.println("- " + q));
        result.questions().forEach(q -> System.out.println("- " + q));
    }

    private String example() {
        return """
                diff --git a/.eslintrc.json b/.eslintrc.json
                index e8e37be..bfb69c7 100644
                --- a/.eslintrc.json
                +++ b/.eslintrc.json
                @@ -42,6 +42,14 @@
                         "unnamedComponents": "arrow-function"
                       }
                     ],
                +    "import/no-extraneous-dependencies": [
                +      "error",
                +      {
                +        "devDependencies": [
                +          "**/mocks/**"
                +        ]
                +      }
                +    ],
                     "react/jsx-curly-brace-presence": ["warn", { "props": "always", "children": "always" }],
                     "react/jsx-props-no-spreading": "off",
                     "arrow-body-style": "off",
                @@ -62,7 +70,9 @@
                     "better-tailwindcss/no-unregistered-classes": "off",
                     "better-tailwindcss/enforce-consistent-line-wrapping": "off",
                     "react/button-has-type": "off",
                -    "@typescript-eslint/no-unused-vars": "error"
                +    "@typescript-eslint/no-unused-vars": "error",
                +    "class-methods-use-this": "warn",
                +    "react/jsx-no-useless-fragment": "warn"
                   },
                   "ignorePatterns": ["node_modules/"]
                 }
                diff --git a/package.json b/package.json
                index 1c8aebc..0824817 100644
                --- a/package.json
                +++ b/package.json
                @@ -55,6 +55,7 @@
                     "eslint-plugin-react-hooks": "^5.2.0",
                     "husky": "^9.1.7",
                     "lint-staged": "^15.5.2",
                +    "msw": "^2.10.5",
                     "prettier": "^3.5.3",
                     "tailwindcss": "^4",
                     "tw-animate-css": "^1.3.5",
                @@ -74,5 +75,10 @@
                     "npm": "please-use-yarn",
                     "pnpm": "please-use-yarn",
                     "yarn": "^1.22.x"
                +  },
                +  "msw": {
                +    "workerDirectory": [
                +      "public"
                +    ]
                   }
                -}
                +}
                \\ No newline at end of file
                diff --git a/public/mockServiceWorker.js b/public/mockServiceWorker.js
                new file mode 100644
                index 0000000..723b071
                --- /dev/null
                +++ b/public/mockServiceWorker.js
                @@ -0,0 +1,344 @@
                +/* eslint-disable */
                +/* tslint:disable */
                +
                +/**
                + * Mock Service Worker.
                + * @see https://github.com/mswjs/msw
                + * - Please do NOT modify this file.
                + */
                +
                +const PACKAGE_VERSION = '2.10.5'
                +const INTEGRITY_CHECKSUM = 'f5825c521429caf22a4dd13b66e243af'
                +const IS_MOCKED_RESPONSE = Symbol('isMockedResponse')
                +const activeClientIds = new Set()
                +
                +addEventListener('install', function () {
                +  self.skipWaiting()
                +})
                +
                +addEventListener('activate', function (event) {
                +  event.waitUntil(self.clients.claim())
                +})
                +
                +addEventListener('message', async function (event) {
                +  const clientId = Reflect.get(event.source || {}, 'id')
                +
                +  if (!clientId || !self.clients) {
                +    return
                +  }
                +
                +  const client = await self.clients.get(clientId)
                +
                +  if (!client) {
                +    return
                +  }
                +
                +  const allClients = await self.clients.matchAll({
                +    type: 'window',
                +  })
                +
                +  switch (event.data) {
                +    case 'KEEPALIVE_REQUEST': {
                +      sendToClient(client, {
                +        type: 'KEEPALIVE_RESPONSE',
                +      })
                +      break
                +    }
                +
                +    case 'INTEGRITY_CHECK_REQUEST': {
                +      sendToClient(client, {
                +        type: 'INTEGRITY_CHECK_RESPONSE',
                +        payload: {
                +          packageVersion: PACKAGE_VERSION,
                +          checksum: INTEGRITY_CHECKSUM,
                +        },
                +      })
                +      break
                +    }
                +
                +    case 'MOCK_ACTIVATE': {
                +      activeClientIds.add(clientId)
                +
                +      sendToClient(client, {
                +        type: 'MOCKING_ENABLED',
                +        payload: {
                +          client: {
                +            id: client.id,
                +            frameType: client.frameType,
                +          },
                +        },
                +      })
                +      break
                +    }
                +
                +    case 'MOCK_DEACTIVATE': {
                +      activeClientIds.delete(clientId)
                +      break
                +    }
                +
                +    case 'CLIENT_CLOSED': {
                +      activeClientIds.delete(clientId)
                +
                +      const remainingClients = allClients.filter((client) => {
                +        return client.id !== clientId
                +      })
                +
                +      // Unregister itself when there are no more clients
                +      if (remainingClients.length === 0) {
                +        self.registration.unregister()
                +      }
                +
                +      break
                +    }
                +  }
                +})
                +
                +addEventListener('fetch', function (event) {
                +  // Bypass navigation requests.
                +  if (event.request.mode === 'navigate') {
                +    return
                +  }
                +
                +  // Opening the DevTools triggers the "only-if-cached" request
                +  // that cannot be handled by the worker. Bypass such requests.
                +  if (
                +    event.request.cache === 'only-if-cached' &&
                +    event.request.mode !== 'same-origin'
                +  ) {
                +    return
                +  }
                +
                +  // Bypass all requests when there are no active clients.
                +  // Prevents the self-unregistered worked from handling requests
                +  // after it's been deleted (still remains active until the next reload).
                +  if (activeClientIds.size === 0) {
                +    return
                +  }
                +
                +  const requestId = crypto.randomUUID()
                +  event.respondWith(handleRequest(event, requestId))
                +})
                +
                +/**
                + * @param {FetchEvent} event
                + * @param {string} requestId
                + */
                +async function handleRequest(event, requestId) {
                +  const client = await resolveMainClient(event)
                +  const requestCloneForEvents = event.request.clone()
                +  const response = await getResponse(event, client, requestId)
                +
                +  // Send back the response clone for the "response:*" life-cycle events.
                +  // Ensure MSW is active and ready to handle the message, otherwise
                +  // this message will pend indefinitely.
                +  if (client && activeClientIds.has(client.id)) {
                +    const serializedRequest = await serializeRequest(requestCloneForEvents)
                +
                +    // Clone the response so both the client and the library could consume it.
                +    const responseClone = response.clone()
                +
                +    sendToClient(
                +      client,
                +      {
                +        type: 'RESPONSE',
                +        payload: {
                +          isMockedResponse: IS_MOCKED_RESPONSE in response,
                +          request: {
                +            id: requestId,
                +            ...serializedRequest,
                +          },
                +          response: {
                +            type: responseClone.type,
                +            status: responseClone.status,
                +            statusText: responseClone.statusText,
                +            headers: Object.fromEntries(responseClone.headers.entries()),
                +            body: responseClone.body,
                +          },
                +        },
                +      },
                +      responseClone.body ? [serializedRequest.body, responseClone.body] : [],
                +    )
                +  }
                +
                +  return response
                +}
                +
                +/**
                + * Resolve the main client for the given event.
                + * Client that issues a request doesn't necessarily equal the client
                + * that registered the worker. It's with the latter the worker should
                + * communicate with during the response resolving phase.
                + * @param {FetchEvent} event
                + * @returns {Promise<Client | undefined>}
                + */
                +async function resolveMainClient(event) {
                +  const client = await self.clients.get(event.clientId)
                +
                +  if (activeClientIds.has(event.clientId)) {
                +    return client
                +  }
                +
                +  if (client?.frameType === 'top-level') {
                +    return client
                +  }
                +
                +  const allClients = await self.clients.matchAll({
                +    type: 'window',
                +  })
                +
                +  return allClients
                +    .filter((client) => {
                +      // Get only those clients that are currently visible.
                +      return client.visibilityState === 'visible'
                +    })
                +    .find((client) => {
                +      // Find the client ID that's recorded in the
                +      // set of clients that have registered the worker.
                +      return activeClientIds.has(client.id)
                +    })
                +}
                +
                +/**
                + * @param {FetchEvent} event
                + * @param {Client | undefined} client
                + * @param {string} requestId
                + * @returns {Promise<Response>}
                + */
                +async function getResponse(event, client, requestId) {
                +  // Clone the request because it might've been already used
                +  // (i.e. its body has been read and sent to the client).
                +  const requestClone = event.request.clone()
                +
                +  function passthrough() {
                +    // Cast the request headers to a new Headers instance
                +    // so the headers can be manipulated with.
                +    const headers = new Headers(requestClone.headers)
                +
                +    // Remove the "accept" header value that marked this request as passthrough.
                +    // This prevents request alteration and also keeps it compliant with the
                +    // user-defined CORS policies.
                +    const acceptHeader = headers.get('accept')
                +    if (acceptHeader) {
                +      const values = acceptHeader.split(',').map((value) => value.trim())
                +      const filteredValues = values.filter(
                +        (value) => value !== 'msw/passthrough',
                +      )
                +
                +      if (filteredValues.length > 0) {
                +        headers.set('accept', filteredValues.join(', '))
                +      } else {
                +        headers.delete('accept')
                +      }
                +    }
                +
                +    return fetch(requestClone, { headers })
                +  }
                +
                +  // Bypass mocking when the client is not active.
                +  if (!client) {
                +    return passthrough()
                +  }
                +
                +  // Bypass initial page load requests (i.e. static assets).
                +  // The absence of the immediate/parent client in the map of the active clients
                +  // means that MSW hasn't dispatched the "MOCK_ACTIVATE" event yet
                +  // and is not ready to handle requests.
                +  if (!activeClientIds.has(client.id)) {
                +    return passthrough()
                +  }
                +
                +  // Notify the client that a request has been intercepted.
                +  const serializedRequest = await serializeRequest(event.request)
                +  const clientMessage = await sendToClient(
                +    client,
                +    {
                +      type: 'REQUEST',
                +      payload: {
                +        id: requestId,
                +        ...serializedRequest,
                +      },
                +    },
                +    [serializedRequest.body],
                +  )
                +
                +  switch (clientMessage.type) {
                +    case 'MOCK_RESPONSE': {
                +      return respondWithMock(clientMessage.data)
                +    }
                +
                +    case 'PASSTHROUGH': {
                +      return passthrough()
                +    }
                +  }
                +
                +  return passthrough()
                +}
                +
                +/**
                + * @param {Client} client
                + * @param {any} message
                + * @param {Array<Transferable>} transferrables
                + * @returns {Promise<any>}
                + */
                +function sendToClient(client, message, transferrables = []) {
                +  return new Promise((resolve, reject) => {
                +    const channel = new MessageChannel()
                +
                +    channel.port1.onmessage = (event) => {
                +      if (event.data && event.data.error) {
                +        return reject(event.data.error)
                +      }
                +
                +      resolve(event.data)
                +    }
                +
                +    client.postMessage(message, [
                +      channel.port2,
                +      ...transferrables.filter(Boolean),
                +    ])
                +  })
                +}
                +
                +/**
                + * @param {Response} response
                + * @returns {Response}
                + */
                +function respondWithMock(response) {
                +  // Setting response status code to 0 is a no-op.
                +  // However, when responding with a "Response.error()", the produced Response
                +  // instance will have status code set to 0. Since it's not possible to create
                +  // a Response instance with status code 0, handle that use-case separately.
                +  if (response.status === 0) {
                +    return Response.error()
                +  }
                +
                +  const mockedResponse = new Response(response.body, response)
                +
                +  Reflect.defineProperty(mockedResponse, IS_MOCKED_RESPONSE, {
                +    value: true,
                +    enumerable: true,
                +  })
                +
                +  return mockedResponse
                +}
                +
                +/**
                + * @param {Request} request
                + */
                +async function serializeRequest(request) {
                +  return {
                +    url: request.url,
                +    mode: request.mode,
                +    method: request.method,
                +    headers: Object.fromEntries(request.headers.entries()),
                +    cache: request.cache,
                +    credentials: request.credentials,
                +    destination: request.destination,
                +    integrity: request.integrity,
                +    redirect: request.redirect,
                +    referrer: request.referrer,
                +    referrerPolicy: request.referrerPolicy,
                +    body: await request.arrayBuffer(),
                +    keepalive: request.keepalive,
                +  }
                +}
                diff --git a/src/apis/github/github.api.ts b/src/apis/github/github.api.ts
                index 5e4d072..81c8cba 100644
                --- a/src/apis/github/github.api.ts
                +++ b/src/apis/github/github.api.ts
                @@ -1,5 +1,3 @@
                -/* eslint-disable class-methods-use-this */
                -
                 export const GITHUB_API_URL = 'https://github.com';
                \s
                 class GithubApi {
                diff --git a/src/app/layout.tsx b/src/app/layout.tsx
                index 6190826..b92d475 100644
                --- a/src/app/layout.tsx
                +++ b/src/app/layout.tsx
                @@ -2,7 +2,9 @@ import type { Metadata } from 'next';
                 import { ReactNode } from 'react';
                \s
                 import GASuspense from '@/components/common/GA/GASuspense';
                +import { initMSW } from '@/mocks';
                 import ModalProvider from '@/providers/ModalContext';
                +import MSWClientProvider from '@/providers/MSWClientProvider';
                 import QueryProvider from '@/providers/QueryProvider';
                \s
                 import './globals.css';
                @@ -26,10 +28,15 @@ export default function RootLayout({
                 }: Readonly<{
                   children: ReactNode;
                 }>) {
                +  if (process.env.NEXT_PUBLIC_MOCK_API === 'enabled') {
                +    initMSW();
                +  }
                +
                   return (
                     <html lang={'ko-KR'}>
                       <QueryProvider>
                         <body>
                +          {process.env.NEXT_PUBLIC_MOCK_API === 'enabled' ? <MSWClientProvider /> : null}
                           <ModalProvider>{children}</ModalProvider>
                           <div id={'portal'} />
                           <GASuspense />
                diff --git a/src/constants/domain.ts b/src/constants/domain.ts
                new file mode 100644
                index 0000000..74f1236
                --- /dev/null
                +++ b/src/constants/domain.ts
                @@ -0,0 +1,2 @@
                +export const ITEMS_PER_PAGE = 5;
                +export const NO_PR_IN_REPOSITORY_ID = 20010903;
                diff --git a/src/constants/routes.ts b/src/constants/routes.ts
                index aff85dc..249683f 100644
                --- a/src/constants/routes.ts
                +++ b/src/constants/routes.ts
                @@ -4,6 +4,42 @@ export const ROUTES = {
                     LANDING: '/landing',
                     AUTH_GITHUB: '/auth/github',
                     REPOLINK: '/repolink',
                -    RETROSPECTIVE: (prId: number | undefined) => `/retrospective/${prId}`,
                +    RETROSPECTIVE: (pullRequestId: number) => `/retrospective/${pullRequestId}`,
                +  },
                +  API: {
                +    // Auth API
                +    LOGOUT_V1: '/api/v1/auth/logout',
                +    REISSUE_TOKEN_V1: '/api/v1/auth/github/refresh',
                +    LOGOUT: '/api/auth/logout',
                +    ISSUE_TOKEN: '/api/auth/github',
                +    REISSUE_TOKEN: '/api/auth/github/refresh',
                +
                +    // Question API
                +    UPDATE_ALL_ANSWERS: '/api/questions/answer',
                +    CREATE_ANSWER: (questionId: number | string) => `/api/questions/${questionId}/answer`,
                +    DELETE_ANSWER: (answerId: number | string) => `/api/questions/answer/${answerId}`,
                +    UPDATE_ANSWER: (answerId: number | string) => `/api/questions/answer/${answerId}`,
                +
                +    // GitHub API
                +    REGISTER_WEBHOOK: (repositoryId: number | string) => `/api/v1/github/repositories/${repositoryId}/webhooks`,
                +
                +    // Repository API
                +    SAVE_REPOSITORY: '/api/repositories',
                +    REPOSITORY_PULL_REQUESTS: (repositoryId: number | string) => `/api/repositories/${repositoryId}/pull-requests`,
                +    ENTIRE_PULL_REQUESTS: '/api/repositories/pull-requests',
                +    MY_REPOSITORIES: '/api/repositories/me',
                +    DELETE_REPOSITORY: (repositoryId: number | string) => `/api/repositories/${repositoryId}`,
                +
                +    // Pull Request API
                +    UPDATE_PULL_REQUEST_TO_DONE: (pullRequestId: number | string) => `/api/pull-requests/${pullRequestId}/done`,
                +    GET_PULL_REQUEST: (pullRequestId: number | string) => `/api/repositories/pull-requests/${pullRequestId}`,
                +    GET_DETAIL_PULL_REQUEST: (pullRequestId: number | string) => `/api/pull-requests/${pullRequestId}`,
                +    GET_PULL_REQUEST_RANKING: '/api/pull-requests/ranking',
                +
                +    // User API
                +    GET_MY_INFO: '/api/users/me',
                +
                +    // Ping API
                +    PING: '/api/ping',
                   },
                 } as const;
                diff --git a/src/middleware.ts b/src/middleware.ts
                index 64b5536..a793066 100644
                --- a/src/middleware.ts
                +++ b/src/middleware.ts
                @@ -19,7 +19,7 @@ export async function middleware(req: NextRequest) {
                     return pathname === pattern || pathname.startsWith(pattern);
                   });
                \s
                -  if (isProtected) {
                +  if (process.env.NEXT_PUBLIC_MOCK_API !== 'enabled' && isProtected) {
                     const token = req.cookies.get(TOKEN_KEY)?.value;
                \s
                     if (!token) {
                diff --git a/src/mocks/browser.ts b/src/mocks/browser.ts
                new file mode 100644
                index 0000000..c680017
                --- /dev/null
                +++ b/src/mocks/browser.ts
                @@ -0,0 +1,5 @@
                +import { setupWorker } from 'msw/browser';
                +
                +import handlers from '@/mocks/handlers';
                +
                +export const worker = setupWorker(...handlers);
                diff --git a/src/mocks/handlers.ts b/src/mocks/handlers.ts
                new file mode 100644
                index 0000000..c76fa82
                --- /dev/null
                +++ b/src/mocks/handlers.ts
                @@ -0,0 +1,8 @@
                +import { type HttpHandler } from 'msw';
                +
                +import repositoriesHandler from '@/mocks/handlers/repositoriesHandler';
                +import userHandler from '@/mocks/handlers/userHandler';
                +
                +const handlers: HttpHandler[] = [...userHandler, ...repositoriesHandler];
                +
                +export default handlers;
                diff --git a/src/mocks/handlers/repositoriesHandler.ts b/src/mocks/handlers/repositoriesHandler.ts
                new file mode 100644
                index 0000000..ce04b49
                --- /dev/null
                +++ b/src/mocks/handlers/repositoriesHandler.ts
                @@ -0,0 +1,34 @@
                +import { HttpResponse, http } from 'msw';
                +
                +import { NO_PR_IN_REPOSITORY_ID } from '@/constants/domain';
                +import { ROUTES } from '@/constants/routes';
                +import getEntirePullRequests from '@/mocks/responses/repositories/getEntirePullRequests.json';
                +import getPullRequest from '@/mocks/responses/repositories/getPullRequest.json';
                +import getRepositoriesMe from '@/mocks/responses/repositories/getRepositoriesMe.json';
                +import getRepositoryPullRequests from '@/mocks/responses/repositories/getRepositoryPullRequests.json';
                +
                +const repositoriesHandler = [
                +  http.get(`*${ROUTES.API.MY_REPOSITORIES}`, () => {
                +    return HttpResponse.json(getRepositoriesMe, { status: 200 });
                +  }),
                +
                +  http.get(`*${ROUTES.API.REPOSITORY_PULL_REQUESTS(':repositoryId')}`, ({ params }) => {
                +    const { repositoryId } = params;
                +
                +    if (Number(repositoryId) === NO_PR_IN_REPOSITORY_ID) {
                +      return new HttpResponse(null, { status: 404 });
                +    }
                +
                +    return HttpResponse.json(getRepositoryPullRequests, { status: 200 });
                +  }),
                +
                +  http.get(`*${ROUTES.API.ENTIRE_PULL_REQUESTS}`, () => {
                +    return HttpResponse.json(getEntirePullRequests, { status: 200 });
                +  }),
                +
                +  http.get(`*${ROUTES.API.GET_PULL_REQUEST(':pullRequestId')}`, () => {
                +    return HttpResponse.json(getPullRequest, { status: 200 });
                +  }),
                +];
                +
                +export default repositoriesHandler;
                diff --git a/src/mocks/handlers/userHandler.ts b/src/mocks/handlers/userHandler.ts
                new file mode 100644
                index 0000000..32ceb20
                --- /dev/null
                +++ b/src/mocks/handlers/userHandler.ts
                @@ -0,0 +1,12 @@
                +import { HttpResponse, http } from 'msw';
                +
                +import { ROUTES } from '@/constants/routes';
                +import getMyInfo from '@/mocks/responses/user/getMyInfo.json';
                +
                +const userHandler = [
                +  http.get(`*${ROUTES.API.GET_MY_INFO}`, () => {
                +    return HttpResponse.json(getMyInfo, { status: 200 });
                +  }),
                +];
                +
                +export default userHandler;
                diff --git a/src/mocks/index.ts b/src/mocks/index.ts
                new file mode 100644
                index 0000000..aa24ca6
                --- /dev/null
                +++ b/src/mocks/index.ts
                @@ -0,0 +1,9 @@
                +export async function initMSW() {
                +  if (typeof window === 'undefined') {
                +    const { server } = await import('./server');
                +    server.listen();
                +  } else {
                +    const { worker } = await import('./browser');
                +    worker.start();
                +  }
                +}
                diff --git a/src/mocks/responses/repositories/getEntirePullRequests.json b/src/mocks/responses/repositories/getEntirePullRequests.json
                new file mode 100644
                index 0000000..a4c3e75
                --- /dev/null
                +++ b/src/mocks/responses/repositories/getEntirePullRequests.json
                @@ -0,0 +1,44 @@
                +{
                +  "pullRequests": [
                +    {
                +      "id": 100,
                +      "title": "[FIX] 서버 장애 대응",
                +      "recordStatus": "PENDING",
                +      "mergedAt": "2025-05-01",
                +      "summary": "이번 장애에서의 문제 상황과 대응 과정을 정리하였습니다.",
                +      "tag": "feat"
                +    },
                +    {
                +      "id": 101,
                +      "title": "[ADD] CI/CD 파이프라인 구축",
                +      "recordStatus": "PROGRESS",
                +      "mergedAt": "2025-05-02",
                +      "summary": "GitHub Actions를 활용한 CI/CD 파이프라인을 구축하여 자동 배포 시스템을 마련했습니다.",
                +      "tag": "chore"
                +    },
                +    {
                +      "id": 102,
                +      "title": "[FEAT] 사용자 프로필 페이지 구현",
                +      "recordStatus": "PENDING",
                +      "mergedAt": "2025-05-03",
                +      "summary": "사용자가 자신의 정보를 확인하고 수정할 수 있는 프로필 페이지를 개발 중입니다.",
                +      "tag": "feat"
                +    },
                +    {
                +      "id": 103,
                +      "title": "[REFACTOR] 인증 로직 개선",
                +      "recordStatus": "PROGRESS",
                +      "mergedAt": "2025-05-04",
                +      "summary": "기존 인증 로직의 가독성과 유지보수성을 향상시키기 위해 코드를 리팩토링했습니다.",
                +      "tag": "refactor"
                +    },
                +    {
                +      "id": 104,
                +      "title": "[DOCS] API 문서 업데이트",
                +      "recordStatus": "DONE",
                +      "mergedAt": "2025-05-05",
                +      "summary": "최신 API 변경사항을 반영하여 개발자 문서를 업데이트했습니다.",
                +      "tag": "docs"
                +    }
                +  ]
                +}
                diff --git a/src/mocks/responses/repositories/getPullRequest.json b/src/mocks/responses/repositories/getPullRequest.json
                new file mode 100644
                index 0000000..99c8496
                --- /dev/null
                +++ b/src/mocks/responses/repositories/getPullRequest.json
                @@ -0,0 +1,60 @@
                +{
                +  "id": 100,
                +  "title": "[FIX] 서버 장애 대응",
                +  "recordStatus": "PENDING",
                +  "mergedAt": "2025-05-01",
                +  "summary": "이번 장애에서의 문제 상황과 대응 과정을 정리하였습니다.",
                +  "tag": "feat",
                +  "pullRequestUrl": "/",
                +  "categories": ["성능", "가독성", "테스트"],
                +  "questions": [
                +    {
                +      "id": 200,
                +      "isSelected": true,
                +      "category": "성능",
                +      "content": "성능적으로 좋은 선택이라 생각하나요?",
                +      "createdAt": "2025-06-24T15:29:45Z",
                +      "updatedAt": "2025-06-24T15:29:45Z"
                +    },
                +    {
                +      "id": 201,
                +      "isSelected": false,
                +      "category": "가독성",
                +      "content": "이 코드는 다른 사람이 쉽게 이해할 수 있나요?",
                +      "createdAt": "2025-06-25T10:00:00Z",
                +      "updatedAt": "2025-06-25T10:00:00Z"
                +    },
                +    {
                +      "id": 202,
                +      "isSelected": false,
                +      "category": "테스트",
                +      "content": "작성한 테스트 코드가 충분한가요?",
                +      "createdAt": "2025-06-26T11:30:00Z",
                +      "updatedAt": "2025-06-26T11:30:00Z"
                +    },
                +    {
                +      "id": 203,
                +      "isSelected": true,
                +      "category": "성능",
                +      "content": "성능적으로 좋은 선택이라 생각하나요? 22",
                +      "createdAt": "2025-06-24T15:29:45Z",
                +      "updatedAt": "2025-06-24T15:29:45Z"
                +    },
                +    {
                +      "id": 204,
                +      "isSelected": false,
                +      "category": "가독성",
                +      "content": "이 코드는 다른 사람이 쉽게 이해할 수 있나요? 22",
                +      "createdAt": "2025-06-25T10:00:00Z",
                +      "updatedAt": "2025-06-25T10:00:00Z"
                +    },
                +    {
                +      "id": 205,
                +      "isSelected": false,
                +      "category": "테스트",
                +      "content": "작성한 테스트 코드가 충분한가요? 22",
                +      "createdAt": "2025-06-26T11:30:00Z",
                +      "updatedAt": "2025-06-26T11:30:00Z"
                +    }
                +  ]
                +}
                diff --git a/src/mocks/responses/repositories/getRepositoriesMe.json b/src/mocks/responses/repositories/getRepositoriesMe.json
                new file mode 100644
                index 0000000..03fd71c
                --- /dev/null
                +++ b/src/mocks/responses/repositories/getRepositoriesMe.json
                @@ -0,0 +1,14 @@
                +{
                +  "repositories": [
                +    {
                +      "id": 1,
                +      "name": "첫번째 레포",
                +      "pullRequestCount": 5
                +    },
                +    {
                +      "id": 20010903,
                +      "name": "두번째 레포",
                +      "pullRequestCount": 3
                +    }
                +  ]
                +}
                diff --git a/src/mocks/responses/repositories/getRepositoryPullRequests.json b/src/mocks/responses/repositories/getRepositoryPullRequests.json
                new file mode 100644
                index 0000000..9da3e9e
                --- /dev/null
                +++ b/src/mocks/responses/repositories/getRepositoryPullRequests.json
                @@ -0,0 +1,44 @@
                +{
                +  "pullRequests": [
                +    {
                +      "id": 301,
                +      "title": "[FEAT] 다크 모드 지원 추가",
                +      "recordStatus": "DONE",
                +      "mergedAt": "2025-06-10",
                +      "summary": "사용자 경험 향상을 위해 애플리케이션 전체에 다크 모드 테마를 적용했습니다.",
                +      "tag": "feat"
                +    },
                +    {
                +      "id": 302,
                +      "title": "[FIX] 로그인 시 간헐적 500 에러 수정",
                +      "recordStatus": "PROGRESS",
                +      "mergedAt": "2025-06-11",
                +      "summary": "특정 조건에서 로그인 요청 시 발생하던 서버 내부 오류를 해결하고 있습니다.",
                +      "tag": "fix"
                +    },
                +    {
                +      "id": 303,
                +      "title": "[CHORE] 의존성 라이브러리 버전 업데이트",
                +      "recordStatus": "DONE",
                +      "mergedAt": "2025-06-12",
                +      "summary": "보안 및 성능 개선을 위해 주요 npm 패키지들을 최신 버전으로 업데이트했습니다.",
                +      "tag": "chore"
                +    },
                +    {
                +      "id": 304,
                +      "title": "[REFACTOR] 전역 상태 관리 로직 개선",
                +      "recordStatus": "PENDING",
                +      "mergedAt": "2025-06-13",
                +      "summary": "기존 Context API 기반 상태 관리를 Recoil로 마이그레이션하여 성능을 최적화합니다.",
                +      "tag": "refactor"
                +    },
                +    {
                +      "id": 305,
                +      "title": "[TEST] E2E 테스트 케이스 추가",
                +      "recordStatus": "DONE",
                +      "mergedAt": "2025-06-14",
                +      "summary": "주요 사용자 플로우에 대한 Cypress E2E 테스트 코드를 추가하여 안정성을 높였습니다.",
                +      "tag": "test"
                +    }
                +  ]
                +}
                diff --git a/src/mocks/responses/user/getMyInfo.json b/src/mocks/responses/user/getMyInfo.json
                new file mode 100644
                index 0000000..ec5535e
                --- /dev/null
                +++ b/src/mocks/responses/user/getMyInfo.json
                @@ -0,0 +1,5 @@
                +{
                +  "id": 1,
                +  "nickname": "김유저",
                +  "profileImageUrl": "https://avatars.githubusercontent.com/u/96560039?s=80&v=4"
                +}
                diff --git a/src/mocks/server.ts b/src/mocks/server.ts
                new file mode 100644
                index 0000000..c4db1a8
                --- /dev/null
                +++ b/src/mocks/server.ts
                @@ -0,0 +1,5 @@
                +import { setupServer } from 'msw/node';
                +
                +import handlers from '@/mocks/handlers';
                +
                +export const server = setupServer(...handlers);
                diff --git a/src/providers/MSWClientProvider.tsx b/src/providers/MSWClientProvider.tsx
                new file mode 100644
                index 0000000..db3994f
                --- /dev/null
                +++ b/src/providers/MSWClientProvider.tsx
                @@ -0,0 +1,13 @@
                +'use client';
                +
                +import { useEffect } from 'react';
                +
                +import { initMSW } from '@/mocks';
                +
                +export default function MSWClientProvider() {
                +  useEffect(() => {
                +    initMSW();
                +  }, []);
                +
                +  return null;
                +}
                diff --git a/tsconfig.json b/tsconfig.json
                index 77f7e72..a89ba4b 100644
                --- a/tsconfig.json
                +++ b/tsconfig.json
                @@ -23,5 +23,5 @@
                     }
                   },
                   "include": ["next-env.d.ts", "**/*.ts", "**/*.tsx", ".next/types/**/*.ts", "@types/*.d.ts"],
                -  "exclude": ["node_modules"]
                +  "exclude": ["node_modules", "public/mockServiceWorker.js"]
                 }
                diff --git a/yarn.lock b/yarn.lock
                index f8d7da3..b1226e5 100644
                --- a/yarn.lock
                +++ b/yarn.lock
                @@ -5411,6 +5411,30 @@ ms@^2.1.1, ms@^2.1.3:
                   resolved "https://registry.yarnpkg.com/ms/-/ms-2.1.3.tgz#574c8138ce1d2b5861f0b44579dbadd60c6615b2"
                   integrity sha512-6FlzubTLZG3J2a/NVCAleEhjzq5oxgHyaCU9yYXvcLsvoVaHJq/s5xXI6/XXP6tz7R9xAOtHnSO/tXtF3WRTlA==
                \s
                +msw@^2.10.5:
                +  version "2.10.5"
                +  resolved "https://registry.yarnpkg.com/msw/-/msw-2.10.5.tgz#3e43f12e97581c260bf38d8817732b9fec3bfdb0"
                +  integrity sha512-0EsQCrCI1HbhpBWd89DvmxY6plmvrM96b0sCIztnvcNHQbXn5vqwm1KlXslo6u4wN9LFGLC1WFjjgljcQhe40A==
                +  dependencies:
                +    "@bundled-es-modules/cookie" "^2.0.1"
                +    "@bundled-es-modules/statuses" "^1.0.1"
                +    "@bundled-es-modules/tough-cookie" "^0.1.6"
                +    "@inquirer/confirm" "^5.0.0"
                +    "@mswjs/interceptors" "^0.39.1"
                +    "@open-draft/deferred-promise" "^2.2.0"
                +    "@open-draft/until" "^2.1.0"
                +    "@types/cookie" "^0.6.0"
                +    "@types/statuses" "^2.0.4"
                +    graphql "^16.8.1"
                +    headers-polyfill "^4.0.2"
                +    is-node-process "^1.2.0"
                +    outvariant "^1.4.3"
                +    path-to-regexp "^6.3.0"
                +    picocolors "^1.1.1"
                +    strict-event-emitter "^0.5.1"
                +    type-fest "^4.26.1"
                +    yargs "^17.7.2"
                +
                 msw@^2.7.1:
                   version "2.10.3"
                   resolved "https://registry.yarnpkg.com/msw/-/msw-2.10.3.tgz#accd0925d2852e9aaa2c86d4fdd724288fee5f35"
                
                """;
    }
}
