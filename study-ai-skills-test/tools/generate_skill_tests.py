#!/usr/bin/env python3
"""
Skill Test Suite Generator
Generates test cases for all available skills in the session.
Re-run to regenerate test cases from the embedded skill inventory.
"""
import json
import os
from datetime import datetime

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
TESTS_DIR = os.path.join(ROOT, "tests")
CASES_DIR = os.path.join(TESTS_DIR, "cases")
REPORT_DIR = os.path.join(ROOT, "docs", "report")

os.makedirs(CASES_DIR, exist_ok=True)
os.makedirs(REPORT_DIR, exist_ok=True)

TODAY = datetime.now().strftime("%Y%m%d")

# Skill inventory: (id, category, description, trigger, prerequisites, test_prompt, expected_behavior, assertions, requires_external)
SKILLS = [
    # ===== TRAE (builtin) =====
    ("TRAE-browseruse", "trae",
     "Browser automation: navigate URLs, scrape content, test frontend UI, multi-step browser actions.",
     "user wants to browse websites / access URLs / scrape web / test frontend UI / browser interaction",
     ["A live http(s) URL to test against", "Browser environment available"],
     "Open https://example.com and tell me the page title and the first heading text.",
     "Skill loads browser-use guidance; agent navigates to URL, reads DOM, returns title (Example Domain) and h1 (Example Domain).",
     ["Response mentions page title 'Example Domain'", "Response mentions heading 'Example Domain'", "Did not fabricate content"],
     True),

    ("TRAE-code-review", "trae",
     "General code review of an MR/PR, commit, branch, or code diff (quality/correctness/maintainability/perf/best-practices).",
     "user explicitly asks for a general code review of MR/PR/commit/branch/diff",
     ["A code diff or MR/PR to review"],
     "Review this diff for quality issues:\n```diff\n- if (x == null) return null;\n+ if (x == null) {\n+   return null;\n+ }\n```",
     "Skill loads; agent returns structured findings covering correctness, maintainability, and best practices; does not treat as security audit.",
     ["Output contains review findings", "Findings reference the provided diff", "No security-audit framing"],
     False),

    ("TRAE-debugger", "trae",
     "Debug complex issues via runtime evidence collection; starts a debug server, scientific workflow (hypothesize->instrument->reproduce->analyze->fix->verify).",
     "debugging complex issues that need runtime evidence, or repeated static analysis failed",
     ["A reproducible bug scenario", "Permission to run a debug server"],
     "I have a Spring Boot endpoint that returns 500 on POST /api/order. Help me debug it systematically.",
     "Skill loads; agent follows scientific debugging workflow, proposes hypotheses, asks for/collects runtime evidence, not just guesses.",
     ["Mentions hypotheses or evidence collection", "Asks for runtime info (logs/trace) rather than guessing the fix outright"],
     False),

    ("TRAE-generate-mini-app", "trae",
     "Create/modify runnable Taro-based mini-program code (WeChat/Alipay/Douyin mini-program).",
     "user explicitly asks to build/generate/develop/edit a mini-program",
     ["Clear mini-program feature requirement"],
     "Generate a WeChat mini-program with one page showing a button that logs 'hello' on tap.",
     "Skill loads; agent emits Taro mini-program files (page config, component) rather than generic web code.",
     ["Output contains mini-program / Taro files", "Code is runnable mini-program structure", "Not plain React web app"],
     False),

    ("TRAE-plan-mode", "trae",
     "Research bounded repository change, write one implementation plan, get approval, then execute.",
     "repository research or clarification needed to determine implementation steps; single plan with approval gate",
     ["A repository with code to change"],
     "Plan how to add a health-check endpoint to this Spring Boot project, then wait for my approval.",
     "Skill loads; agent researches repo, writes a plan, and STOPS for approval before implementing.",
     ["A plan is produced", "Agent does not implement before approval", "Plan references actual repo structure"],
     False),

    ("TRAE-security-review", "trae",
     "Code security scanning / vulnerability audit of MR/PR/commit/branch/diff/uncommitted changes.",
     "user requests a security review / vulnerability scan",
     ["A code diff or file set to audit"],
     "Security-review this code:\n```python\nquery = f\"SELECT * FROM users WHERE name='{name}'\"\n```",
     "Skill loads; agent reports demonstrably exploitable, change-introduced risks (here: SQL injection).",
     ["Identifies SQL injection risk", "Risk is tied to the provided code", "Recommendations are concrete"],
     False),

    ("TRAE-spec-mode", "trae",
     "Carry substantial changes end-to-end: clarify requirements, acceptance criteria, plan, implement, verify, review, remediate.",
     "complex/high-impact multi-component change with ambiguity or quality risk",
     ["A substantial feature/change request"],
     "Use spec mode to add user login (JWT) to this backend project end-to-end.",
     "Skill loads; agent produces spec artifacts (requirements, acceptance criteria, plan) before implementation, with traceability.",
     ["Produces spec/requirements artifacts", "Defines acceptance criteria", "Follows plan->implement->verify flow"],
     False),

    # ===== common =====
    ("common-api-doc", "common",
     "Generate API documentation from OpenAPI spec; supports Swagger UI and ReDoc.",
     "need API docs / OpenAPI / Swagger / ReDoc",
     ["An OpenAPI spec or controller source"],
     "Generate API docs (Swagger UI + ReDoc) for a REST endpoint GET /api/users/{id} returning a User JSON.",
     "Skill loads; produces OpenAPI spec and references Swagger UI/ReDoc presentation.",
     ["Output includes OpenAPI YAML/JSON", "Mentions Swagger UI or ReDoc", "Endpoint GET /api/users/{id} is documented"],
     False),

    ("common-backend-unit-test", "common",
     "Backend unit test guidance and generation; JUnit 5, Mockito.",
     "need backend unit tests (Java/JUnit/Mockito)",
     ["A Java class/method to test"],
     "Write JUnit 5 + Mockito unit tests for a UserService.register(User) method that calls userRepository.save.",
     "Skill loads; produces JUnit 5 test using Mockito to mock repository, with assertions and verify.",
     ["Uses @Test (JUnit 5)", "Uses Mockito (@Mock / when / verify)", "Asserts behavior of register"],
     False),

    ("common-code-review", "common",
     "Code review: code quality checks and improvement suggestions.",
     "need code review / quality check",
     ["A code snippet or file"],
     "Review this Java method for quality:\n```java\npublic int add(int a,int b){return a+b;}\n```",
     "Skill loads; returns quality findings (readability, conventions) and improvement suggestions.",
     ["Provides quality findings", "Suggests improvements", "References the provided code"],
     False),

    ("common-coding", "common",
     "Coding guidance following standards and best practices.",
     "user needs to write code",
     ["A coding requirement"],
     "Write a Java method to reverse a string, following best practices and include a brief explanation.",
     "Skill loads; generates code with explanation following standards (null-safety, readability).",
     ["Produces working Java code", "Includes explanation", "Handles null/empty gracefully"],
     False),

    ("common-contract-generation", "common",
     "API contract document generation and management.",
     "need API contract / interface contract",
     ["An API to contract-ify"],
     "Generate an API contract for POST /api/orders that creates an order (request/response schema).",
     "Skill loads; produces a contract document with request/response schemas and error cases.",
     ["Includes request schema", "Includes response schema", "Lists error cases"],
     False),

    ("common-data-migration", "common",
     "Database migration via Flyway or Liquibase.",
     "need DB migration / Flyway / Liquibase",
     ["A schema change requirement"],
     "Generate a Flyway migration to add a 'email' column to table 'users'.",
     "Skill loads; produces a Flyway migration file (Vx__add_email.sql) compliant with Flyway naming.",
     ["Migration file follows Flyway naming V..__..sql", "ALTER TABLE users ADD COLUMN email", "Mentions Flyway"],
     False),

    ("common-db-design", "common",
     "Database design: table structures and entity classes.",
     "need DB schema / entity design",
     ["Domain entities to model"],
     "Design DB tables and JPA entities for User and Order (one user has many orders).",
     "Skill loads; produces DDL for users/orders with FK and JPA entity classes with relationships.",
     ["Produces DDL with foreign key", "Produces JPA entities", "Models one-to-many relationship"],
     False),

    ("common-design-brainstorming", "common",
     "Design brainstorming before creative implementation: explore intent, path classification, progressive design decisions.",
     "before creative implementation, explore intent and approaches",
     ["A feature/idea to brainstorm"],
     "Brainstorm design approaches for a personal todo app feature before implementing.",
     "Skill loads; agent explores intent, classifies solution paths, and makes progressive design decisions (not jumping to code).",
     ["Explores intent/goals", "Lists multiple solution paths", "Makes progressive decisions, no premature code"],
     False),

    ("common-dockerize", "common",
     "Docker containerization: generate Docker configuration files.",
     "need Dockerfile / docker-compose / containerization",
     ["An app to containerize"],
     "Generate a Dockerfile and docker-compose for a Spring Boot app on port 8080.",
     "Skill loads; produces Dockerfile (multi-stage optional) and docker-compose.yml with port mapping.",
     ["Produces Dockerfile", "Produces docker-compose.yml", "Maps port 8080"],
     False),

    ("common-frontend-playwright-test", "common",
     "Frontend Playwright UI automation test guidance and generation.",
     "need frontend Playwright tests",
     ["A frontend page/feature to test"],
     "Generate a Playwright test that visits a login page, fills credentials, and clicks submit.",
     "Skill loads; produces a Playwright test using page.goto, fill, click, expect assertions.",
     ["Uses Playwright API (page.goto/fill/click/expect)", "Targets login flow", "Contains assertions"],
     False),

    ("common-grill-me", "common",
     "Deeply question the user to validate requirement rationality, necessity, and benefit until consensus.",
     "need to validate/challenge a requirement deeply",
     ["A requirement to grill"],
     "Grill me on this requirement: 'Add a dark mode to the app.'",
     "Skill loads; agent asks probing questions (why, who benefits, alternatives, cost) iteratively until consensus.",
     ["Asks multiple probing questions", "Questions cover why/value/alternatives/cost", "Does not immediately implement"],
     False),

    ("common-project-standard", "common",
     "Project structure, document management, .gitignore standards and best practices.",
     "need project structure / conventions / standards",
     ["A project to standardize"],
     "Recommend project structure and .gitignore for a Java Spring Boot + Vue monorepo.",
     "Skill loads; gives standardized directory layout and .gitignore entries for the stack.",
     ["Provides directory structure", "Provides .gitignore content", "Tailored to Spring Boot + Vue"],
     False),

    ("common-requirement-clarification", "common",
     "Help teams clarify and refine business requirements.",
     "need requirement clarification",
     ["A vague requirement"],
     "Clarify this requirement: 'Build a shopping cart feature.'",
     "Skill loads; agent asks clarifying questions (scope, flows, edge cases) to refine the requirement.",
     ["Asks clarifying questions", "Covers scope/flows/edge cases", "Produces refined requirement"],
     False),

    ("common-spec-driven", "common",
     "Spec-driven: all changes need change description, change plan, change benefit spec docs.",
     "requirement that all changes must be spec-driven",
     ["A change request"],
     "Apply spec-driven process to: add pagination to GET /api/users.",
     "Skill loads; agent produces change description, change plan, and change benefit documents.",
     ["Produces change description", "Produces change plan", "Produces change benefit"],
     False),

    ("common-spring-boot-init", "common",
     "Quickly create Spring Boot project structure with multiple DB/dependency options.",
     "need to scaffold a Spring Boot project",
     ["Project requirements (DB, deps)"],
     "Scaffold a Spring Boot 3 project with Maven, H2, Spring Web, and Spring Data JPA.",
     "Skill loads; produces pom.xml and base package structure with the requested dependencies.",
     ["Produces pom.xml with Spring Boot 3", "Includes H2, Web, Data JPA", "Has base package structure"],
     False),

    ("common-systematic-debugging", "common",
     "Systematic debugging: root-cause first, four-phase structured investigation.",
     "need structured root-cause debugging",
     ["A bug to investigate"],
     "Use systematic debugging to investigate: 'Orders are sometimes duplicated.'",
     "Skill loads; agent follows a four-phase root-cause-first process, not random guesses.",
     ["Follows structured phases", "Root-cause oriented", "Asks for evidence/data"],
     False),

    ("common-technical-writer", "common",
     "Technical writing: standardized README, API docs, tutorials; supports CN<->EN translation.",
     "need README / API doc / tutorial / technical translation",
     ["A project or text to document/translate"],
     "Write a standardized README for a Spring Boot user-service project, with a short English summary too.",
     "Skill loads; produces structured README sections and an English summary.",
     ["README has standard sections (intro, install, usage)", "Includes English summary", "Project-specific content"],
     False),

    ("common-test-driven-development", "common",
     "TDD: red-green-refactor, write tests first.",
     "need TDD / test-first development",
     ["A feature to build test-first"],
     "Apply TDD to implement a StringUtils.isPalindrome method (write the failing test first).",
     "Skill loads; agent writes a failing test first, then implementation, then refactors, describing red-green-refactor.",
     ["Test written before implementation", "Mentions red-green-refactor", "Implementation makes tests pass"],
     False),

    ("common-ui-design-spec", "common",
     "UI design system: colors, typography, accessibility standards.",
     "need UI design system / spec / accessibility",
     ["A product to design for"],
     "Define a UI design spec (color palette, typography scale, accessibility rules) for a SaaS dashboard.",
     "Skill loads; produces color palette, typography scale, and WCAG accessibility rules.",
     ["Includes color palette", "Includes typography scale", "Includes accessibility (WCAG) rules"],
     False),

    ("common-ui-mockup", "common",
     "Generate UI mockups: page draft sketches, wireframes, UI docs from a requirement.",
     "need UI mockup / wireframe",
     ["A page/feature to mock up"],
     "Generate a wireframe and UI doc for a mobile login page.",
     "Skill loads; produces a wireframe (text/ASCII or structured layout) and UI documentation.",
     ["Produces a wireframe/layout", "Includes UI doc", "Targets mobile login page"],
     False),

    ("common-visual-design", "common",
     "Visual design philosophy: distinctive UI/UX, avoid templates and AI-default style.",
     "need distinctive visual design direction",
     ["A product/brand to design"],
     "Give a visual design direction for a boutique coffee brand app that avoids generic AI style.",
     "Skill loads; provides a distinctive design direction (mood, palette, typography) that avoids templates.",
     ["Gives a specific design direction", "States how it avoids generic/AI style", "Includes palette/typography/mood"],
     False),

    ("common-vue-init", "common",
     "Vue project initialization: quickly create a Vue frontend project.",
     "need to scaffold a Vue project",
     ["Vue project requirements"],
     "Scaffold a Vue 3 project with Vite, Pinia, and Vue Router.",
     "Skill loads; produces package.json / project structure with Vue 3, Vite, Pinia, Vue Router.",
     ["Vue 3 + Vite present", "Pinia configured", "Vue Router configured"],
     False),

    # ===== other =====
    ("dynamic-ui", "other",
     "Render visual content inline (diagrams, charts, interactive demos) alongside text.",
     "a compact visual makes the answer clearer",
     ["A concept/numbers to visualize"],
     "Show a small bar chart of sales: Jan=10, Feb=20, Mar=15.",
     "Skill loads; agent invokes PureShowWidget with an SVG/HTML chart rendering the data.",
     ["A visual widget is rendered", "Chart reflects Jan=10, Feb=20, Mar=15", "Inline with text response"],
     False),

    ("gp-agent-handoff", "other",
     "Create a truthful, evidence-bounded handoff to the Master Agent before a General Purpose subagent returns its final result.",
     "immediately before a GP subagent returns its final result",
     ["A GP subagent task completing"],
     "(Internal) A GP subagent finished researching 'DB migration tools'. Produce a handoff summary.",
     "Skill loads; agent writes a concise handoff with evidence, no fabrication, bounded claims.",
     ["Handoff is concise", "Claims are evidence-bounded", "No fabrication"],
     False),

    # ===== personal =====
    ("personal-backend-coding-standard", "personal",
     "Backend coding standards and best practices guidance.",
     "need backend coding standards",
     ["Backend code to standardize"],
     "Review this Java controller against backend coding standards:\n```java\n@GetMapping(\"/list\")\npublic List list(){ return service.findAll(); }\n```",
     "Skill loads; checks against backend standards (DTO use, generics, exception handling, logging).",
     ["Mentions backend standards", "Points out missing generics/DTO/exception handling", "Gives concrete fixes"],
     False),

    ("personal-brief-style", "personal",
     "Concise style: no fluff, caveman-style short direct expressions.",
     "need to enforce brief/caveman response style",
     ["A question to answer briefly"],
     "Answer briefly: what is TDD?",
     "Skill loads; response is short, direct, no fluff (caveman-style).",
     ["Response is very short", "No filler words", "Direct answer"],
     False),

    ("personal-doc-manager", "personal",
     "Personal doc management: changelog, spec, test reports under docs/ with structured subdirs and naming {module}-{content}-{date}.md.",
     "manage change logs / spec docs / test reports in docs/",
     ["A doc to manage"],
     "Where should I store a spec doc for the 'order' module dated today, and what filename?",
     "Skill loads; agent answers with docs/spec/order-spec-YYYYMMDD.md naming convention and structure.",
     ["Mentions docs/spec/ path", "Uses {module}-{content}-{date}.md format", "Correct subdir (spec)"],
     False),

    ("personal-frontend-coding-standard", "personal",
     "Frontend coding standards and best practices guidance.",
     "need frontend coding standards",
     ["Frontend code to standardize"],
     "Review this Vue component against frontend coding standards:\n```vue\n<template><div>{{msg}}</div></template>\n<script>export default{data(){return{msg:'hi'}}}</script>\n```",
     "Skill loads; checks frontend standards (naming, structure, reactivity, eslint conventions).",
     ["Mentions frontend standards", "Points out convention issues", "Gives concrete fixes"],
     False),

    ("personal-java-common", "personal",
     "Java common utilities: date utils, logging utils, based on study-common codebase.",
     "need Java common utilities (date/logging)",
     ["A Java utility need"],
     "Show a common Java date utility to format LocalDateTime to 'yyyy-MM-dd HH:mm:ss'.",
     "Skill loads; provides a date utility (DateTimeFormatter) consistent with study-common style.",
     ["Provides a date utility method", "Uses DateTimeFormatter", "Format yyyy-MM-dd HH:mm:ss"],
     False),

    ("personal-project-runner", "personal",
     "Project start/stop/restart management for Java Spring Boot and Vue, keeping original ports.",
     "need to start/restart/stop Spring Boot or Vue project",
     ["A project to run"],
     "How do I restart a Spring Boot app on its original port using this skill?",
     "Skill loads; agent explains restart keeping original port for Spring Boot (and Vue).",
     ["Mentions Spring Boot restart", "Keeps original port", "Mentions port preservation"],
     False),

    ("personal-solution-research", "personal",
     "Solution research: compare 3-5 solutions (industry benchmarks, big-tech) with evaluation dimensions (functionality, performance, cost, complexity).",
     "need solution comparison / research",
     ["A problem needing solution options"],
     "Research 3-5 solutions for session storage in a distributed system, comparing functionality/performance/cost/complexity.",
     "Skill loads; compares >=3 solutions across the four dimensions with benchmarks.",
     ["Compares >=3 solutions", "Uses functionality/performance/cost/complexity dimensions", "References industry/big-tech"],
     False),

    ("skill-creator", "personal",
     "Create or update project/global skills, or install skills globally.",
     "asked to create/edit/explain/install a skill",
     ["A skill to create or edit"],
     "What does this skill do and when should it be used? (Explain the skill-creator skill).",
     "Skill loads; agent explains skill-creator purpose and usage (note: not a callable tool itself).",
     ["Explains skill-creator purpose", "States when to use it", "Mentions it creates/updates skills"],
     False),

    # ===== lark =====
    ("lark-approval", "lark",
     "Feishu approvals: query/process approval todos/done/instances, search definitions, view details, initiate native approvals.",
     "handle approval tasks / view instances / search or initiate approvals",
     ["Lark authorization (lark-shared auth)", "Approval instance/definition context"],
     "List my pending approval todos in Feishu.",
     "Skill loads; uses lark-approval to fetch pending approval todos (requires auth).",
     ["Routes to lark-approval", "Returns approval todo list (or auth-needed message)", "Does not use lark-task"],
     True),

    ("lark-attendance", "lark",
     "Feishu attendance: query own clock-in records.",
     "query attendance / clock-in records",
     ["Lark authorization"],
     "Show my attendance clock-in records for this week.",
     "Skill loads; returns own attendance records.",
     ["Routes to lark-attendance", "Returns clock-in records", "Own records only"],
     True),

    ("lark-base", "lark",
     "Feishu Base (bitable): tables, fields, records, views, forms, dashboards, workspace, roles, templates.",
     "Base/bitable operations, /base/ or /app/ links",
     ["Lark authorization", "A Base app token"],
     "List all tables in my Feishu Base at https://.../base/xxxx.",
     "Skill loads; lists tables in the specified Base.",
     ["Routes to lark-base", "Lists tables", "Uses base token from URL"],
     True),

    ("lark-calendar", "lark",
     "Feishu calendar: view/search events, create/update events, attendees, free/busy, meeting rooms.",
     "calendar / schedule / meeting room booking",
     ["Lark authorization"],
     "Show my calendar events for tomorrow.",
     "Skill loads; returns calendar events for tomorrow.",
     ["Routes to lark-calendar", "Returns events", "Filtered to tomorrow"],
     True),

    ("lark-contact", "lark",
     "Feishu contacts: resolve name/email to open_id or reverse; search bots/agents.",
     "resolve a name/email to open_id or look up contact info",
     ["Lark authorization", "A name/email or open_id"],
     "Resolve the open_id for user 'zhangsan' in Feishu.",
     "Skill loads; resolves name to open_id.",
     ["Routes to lark-contact", "Returns open_id", "Resolves by name"],
     True),

    ("lark-doc", "lark",
     "Feishu cloud docs (Docx/Wiki): read, create, edit, insert/download images, mind notes.",
     "operate on a doc/wiki (URL/token)",
     ["Lark authorization", "A doc URL/token"],
     "Read the content of this Feishu doc: https://.../docx/xxxx.",
     "Skill loads; reads doc content by token.",
     ["Routes to lark-doc", "Reads doc content", "Uses doc token"],
     True),

    ("lark-drive", "lark",
     "Feishu Drive: file/folder management, upload/download, create folders, copy/move/delete, permissions, comments, import files.",
     "manage Drive files/folders, import files",
     ["Lark authorization", "A file/folder token or URL"],
     "List files in the root of my Feishu Drive.",
     "Skill loads; lists Drive files.",
     ["Routes to lark-drive", "Lists files", "Drive scope"],
     True),

    ("lark-event", "lark",
     "Lark real-time event listening/subscribing/consuming via lark-cli event consume.",
     "need to listen to Lark real-time events",
     ["Lark authorization", "An event key"],
     "Subscribe to Feishu IM message events and print them as they arrive.",
     "Skill loads; streams events via lark-cli event consume.",
     ["Routes to lark-event", "Uses event consume mechanism", "Streams events"],
     True),

    ("lark-im", "lark",
     "Feishu IM: send/reply messages, search chats, manage groups, upload/download files, emoji replies, urgent, interactive cards, card callbacks.",
     "send messages, manage chats/groups, interactive cards",
     ["Lark authorization", "A chat/user target"],
     "Send a 'hello' message to user 'zhangsan' in Feishu.",
     "Skill loads; resolves recipient and sends IM message.",
     ["Routes to lark-im", "Sends message", "Uses correct recipient"],
     True),

    ("lark-mail", "lark",
     "Feishu Mail: draft, send, reply, forward, search, folders, labels, contacts, rules.",
     "email intent: draft/send/search mail",
     ["Lark authorization"],
     "Draft an email to test@example.com with subject 'Hello'.",
     "Skill loads; drafts an email.",
     ["Routes to lark-mail", "Drafts email", "Correct recipient/subject"],
     True),

    ("lark-markdown", "lark",
     "Feishu native Markdown: view, create, upload, edit, compare remote vs local draft.",
     "operate on Feishu native Markdown files",
     ["Lark authorization", "A markdown file token"],
     "Create a new Feishu native Markdown file with content '# Hello'.",
     "Skill loads; creates a native markdown file.",
     ["Routes to lark-markdown", "Creates markdown file", "Content is '# Hello'"],
     True),

    ("lark-meeting", "lark",
     "Feishu meetings: query records/minutes/transcripts, upload/edit minutes, bot participation, real-time meeting content,妙记 search.",
     "query meeting records, minutes,妙记; participate in meetings",
     ["Lark authorization", "A meeting/妙记 id or URL"],
     "Search my Feishu meeting minutes for 'Q3 review'.",
     "Skill loads; searches meeting minutes.",
     ["Routes to lark-meeting", "Searches minutes", "Keyword 'Q3 review'"],
     True),

    ("lark-minutes", "lark",
     "Use only when explicitly specified; delegates to lark-meeting.",
     "explicitly requests lark-minutes",
     ["Lark authorization", "A minute token"],
     "Use lark-minutes to get the transcript for minute token 'xxxx'.",
     "Skill loads; delegates to lark-meeting to retrieve transcript.",
     ["Routes via lark-minutes to lark-meeting", "Returns transcript", "Uses minute token"],
     True),

    ("lark-note", "lark",
     "Use only when explicitly specified; delegates to lark-meeting.",
     "explicitly requests lark-note",
     ["Lark authorization", "A note id"],
     "Use lark-note to fetch note 'xxxx'.",
     "Skill loads; delegates to lark-meeting.",
     ["Routes via lark-note to lark-meeting", "Returns note content", "Uses note id"],
     True),

    ("lark-okr", "lark",
     "Feishu OKR: view/edit OKR cycles, objectives, key results, alignment, metrics, progress.",
     "view or create OKRs",
     ["Lark authorization"],
     "Show my current OKR cycle objectives.",
     "Skill loads; returns current OKR objectives.",
     ["Routes to lark-okr", "Returns OKR objectives", "Current cycle"],
     True),

    ("lark-openapi-explorer", "lark",
     "Discover native Feishu OpenAPI endpoints not wrapped by lark-cli.",
     "need a native OpenAPI not covered by existing lark skills",
     ["Lark authorization", "An API need"],
     "Find a native Feishu OpenAPI to get a user's department by open_id.",
     "Skill loads; finds and documents the native endpoint.",
     ["Routes to lark-openapi-explorer", "Finds a native endpoint", "Documents it"],
     True),

    ("lark-shared", "lark",
     "lark-cli setup/auth: login/status/logout, identity, domain permissions, missing scopes, _notice handling.",
     "lark-cli auth setup or credential issues",
     ["lark-cli installed"],
     "Check my Feishu CLI auth status.",
     "Skill loads; checks lark-cli auth status.",
     ["Routes to lark-shared", "Returns auth status", "Mentions login/logout if needed"],
     True),

    ("lark-sheets", "lark",
     "Feishu Sheets: create, manage sheets, read/write cells, find/replace, charts, pivot tables, formatting.",
     "create or edit spreadsheets, batch read/write, stats",
     ["Lark authorization", "A sheet token"],
     "Read cells A1:C3 from my Feishu sheet 'xxxx'.",
     "Skill loads; reads the specified range.",
     ["Routes to lark-sheets", "Reads range A1:C3", "Uses sheet token"],
     True),

    ("lark-skill-maker", "lark",
     "Create custom skills for lark-cli wrapping atomic APIs or multi-step flows.",
     "create a custom lark-cli skill",
     ["Lark authorization", "A skill spec"],
     "Create a custom lark-cli skill that sends a message and then pins it.",
     "Skill loads; produces a custom skill definition wrapping APIs.",
     ["Routes to lark-skill-maker", "Produces a custom skill", "Wraps message + pin flow"],
     True),

    ("lark-slides", "lark",
     "Feishu slides: create, read, edit slides/pages.",
     "create or edit slides",
     ["Lark authorization", "A slide token"],
     "Create a Feishu slide deck with one title slide.",
     "Skill loads; creates a slide with a title page.",
     ["Routes to lark-slides", "Creates slide", "Has a title slide"],
     True),

    ("lark-task", "lark",
     "Feishu tasks: create todos, update status, subtasks, lists, assignees, attachments, task agents.",
     "create or manage tasks/todos",
     ["Lark authorization"],
     "Create a Feishu task 'Buy milk' due tomorrow.",
     "Skill loads; creates a task with due date.",
     ["Routes to lark-task", "Creates task", "Due tomorrow"],
     True),

    ("lark-vc", "lark",
     "Use only when explicitly specified; delegates to lark-meeting.",
     "explicitly requests lark-vc",
     ["Lark authorization", "A meeting id"],
     "Use lark-vc to query an ongoing meeting by id 'xxxx'.",
     "Skill loads; delegates to lark-meeting.",
     ["Routes via lark-vc to lark-meeting", "Queries meeting", "Uses meeting id"],
     True),

    ("lark-vc-agent", "lark",
     "Use only when explicitly specified; delegates to lark-meeting.",
     "explicitly requests lark-vc-agent",
     ["Lark authorization", "A meeting context"],
     "Use lark-vc-agent to join a meeting as a bot.",
     "Skill loads; delegates to lark-meeting bot participation.",
     ["Routes via lark-vc-agent to lark-meeting", "Bot participation", "Uses meeting context"],
     True),

    ("lark-whiteboard", "lark",
     "Feishu whiteboards in docs: export preview, export node structure, update content.",
     "view/export/edit a whiteboard in a doc",
     ["Lark authorization", "A whiteboard token"],
     "Export my Feishu whiteboard 'xxxx' as a preview image.",
     "Skill loads; exports whiteboard preview.",
     ["Routes to lark-whiteboard", "Exports preview", "Uses whiteboard token"],
     True),

    ("lark-wiki", "lark",
     "Feishu knowledge base (Wiki): spaces, members, nodes, organize docs/shortcuts.",
     "manage Wiki spaces, nodes, members",
     ["Lark authorization"],
     "List nodes in my Feishu Wiki space 'xxxx'.",
     "Skill loads; lists wiki nodes.",
     ["Routes to lark-wiki", "Lists nodes", "Uses space id"],
     True),

    ("lark-workflow-meeting-summary", "lark",
     "Workflow: summarize meeting minutes within a time range into a structured report.",
     "organize meeting minutes / weekly report",
     ["Lark authorization", "A time range"],
     "Summarize my Feishu meeting minutes from this week into a report.",
     "Skill loads; fetches minutes in range and produces a structured report.",
     ["Routes to lark-workflow-meeting-summary", "Summarizes minutes", "Structured report"],
     True),

    ("lark-workflow-standup-report", "lark",
     "Workflow: calendar agenda + tasks -> summary of schedule and open tasks for a date.",
     "understand today/tomorrow/this week schedule and open tasks",
     ["Lark authorization", "A target date"],
     "Give me a standup report for today: my schedule and open tasks.",
     "Skill loads; combines calendar + tasks into a standup summary.",
     ["Routes to lark-workflow-standup-report", "Combines calendar + tasks", "For today's date"],
     True),
]


def main():
    manifest = {
        "generated_at": datetime.now().isoformat(timespec="seconds"),
        "total_skills": len(SKILLS),
        "categories": {},
        "skills": []
    }

    for skill in SKILLS:
        sid, category, description, trigger, prereqs, prompt, expected, assertions, ext = skill
        case_id = f"TC-{sid}"
        entry = {
            "id": sid,
            "case_id": case_id,
            "category": category,
            "description": description,
            "trigger": trigger,
            "prerequisites": prereqs,
            "test_prompt": prompt,
            "expected_behavior": expected,
            "assertions": assertions,
            "requires_external": ext,
            "status": "untested",
        }
        manifest["skills"].append(entry)
        manifest["categories"].setdefault(category, 0)
        manifest["categories"][category] += 1

        # Write case file
        case_path = os.path.join(CASES_DIR, f"{sid}.test.md")
        content = []
        content.append(f"# Test Case: {sid}\n")
        content.append(f"- **Case ID**: `{case_id}`")
        content.append(f"- **Category**: {category}")
        content.append(f"- **Status**: untested")
        content.append(f"- **Requires external setup**: {'yes' if ext else 'no'}\n")
        content.append("## Description\n")
        content.append(f"{description}\n")
        content.append("## Trigger condition\n")
        content.append(f"{trigger}\n")
        content.append("## Prerequisites\n")
        for p in prereqs:
            content.append(f"- {p}")
        content.append("")
        content.append("## Test prompt (send this to the assistant)\n")
        fence = "````" if "```" in prompt else "```"
        content.append(f"{fence}text")
        content.append(prompt)
        content.append(f"{fence}\n")
        content.append("## Expected behavior\n")
        content.append(f"{expected}\n")
        content.append("## Assertions (pass criteria)\n")
        for a in assertions:
            content.append(f"- [ ] {a}")
        content.append("")
        content.append("## Notes / Actual result\n")
        content.append("_fill in after running_\n")
        with open(case_path, "w", encoding="utf-8") as f:
            f.write("\n".join(content))

    # Write manifest
    with open(os.path.join(TESTS_DIR, "manifest.json"), "w", encoding="utf-8") as f:
        json.dump(manifest, f, ensure_ascii=False, indent=2)

    print(f"Generated {len(SKILLS)} test cases in {CASES_DIR}")
    print(f"Categories: {manifest['categories']}")


if __name__ == "__main__":
    main()
