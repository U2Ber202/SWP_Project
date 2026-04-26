# TEST PLAN

Project: V-SNKR Web Application Testing  
Version: 1.0  
Test Plan Date: 23/04/2026  
Prepared by: Codex hỗ trợ soạn theo cấu trúc mẫu người dùng cung cấp

## Revision History

| Date | Version | Author | Description |
|---|---|---|---|
| 23/04/2026 | 1.0 | Codex | Initial test plan based on current project source code, database schema, JSP pages, controllers and unit tests |

## Table of Contents

1. Introduction  
2. Test Strategy  
3. Test Planning  
4. Test Design  
5. Test Execution  
6. Test Closure  
7. Risk and Contingencies  
8. Approval  
9. Appendices

## I. Introduction

### Purpose

This test plan defines the testing scope, strategy, resources, schedule, and completion criteria for the V-SNKR web application. The purpose is to verify that the application works correctly for customer and staff roles, protects user data, processes shopping and order flows reliably, and supports store management operations with acceptable usability and performance.

### Scope

The testing scope covers the main modules identified from the current project source code:

- User authentication: sign up, login, logout, activate account, forgot password, OTP verification, reset/change password.
- Customer features: home page, product listing, category filter, search, product detail, add to cart, update cart, delete cart item, checkout, thank-you page, purchase history, profile management, wallet, voucher usage, feedback submission, contact submission.
- Order and shipping features: order creation, order detail, shipping information, shipping status update, purchase history.
- Store and inventory management: category management, product management, stock import, stock history, store management, home setting management, news management, voucher management, feedback management, contact management.
- Administration and role-based access: manager account, manager category, manager contact, manager news, manager staff, statistics, manage store, admin filter.
- Utility and support components: validation, password hashing, role helper, cart service, email sending, VNPay payment integration, session/cart expiration handling.

### Out of Scope

- Native mobile application testing.
- Third-party provider internal testing for VNPay and email service.
- Browser compatibility outside the selected supported browsers.
- Source-code security audit at infrastructure level.

## II. Test Strategy

### Testing Levels

#### Unit Testing

Verify models and utility classes independently. The project already contains unit tests for model classes and utility classes such as `Account`, `Cart`, `Category`, `Order`, `Product`, `Shipping`, `Voucher`, `PasswordUtil`, `ValidationUtil`, `RoleHelper`, and `CartService`.

#### Integration Testing

Verify interaction between controllers, DAO layer, database, session state, and JSP views. Focus on login flow, cart to checkout flow, order creation, feedback/contact submission, and role-protected pages.

#### System Testing

Validate end-to-end business flows on the deployed web application using the configured SQL Server database and servlet container.

#### Acceptance Testing

Confirm that the delivered system supports key business scenarios for customers, store owners, warehouse managers, shippers, and administrators.

### Testing Types

#### Functional Testing

Validate all key business functions including authentication, browsing products, cart handling, checkout, order creation, shipping, voucher application, feedback, store management, and reporting.

#### GUI Testing

Verify JSP pages, forms, tables, navigation, error messages, and responsive behavior on common desktop screen sizes.

#### Usability Testing

Assess whether customer and staff users can complete frequent tasks easily, especially registration, login, adding products, checkout, and order/shipping management.

#### Security Testing

Check authentication, authorization, protected URLs, invalid session handling, password reset flow, input validation, and common web security risks such as unauthorized access or malicious input submission.

#### Database Testing

Validate CRUD operations, referential integrity, seeded data consistency, and transactional correctness for carts, orders, shipping, feedback, contacts, vouchers, and inventory.

#### Compatibility Testing

Test on supported browsers such as Google Chrome, Microsoft Edge, and Mozilla Firefox on Windows.

#### Performance Testing

Measure page response for high-use functions such as home page, product search, cart operations, checkout, and manager dashboards under moderate concurrent usage.

#### Regression Testing

Re-run critical test cases after fixes to ensure existing flows remain stable.

### Test Environment

- Application type: Java Servlet/JSP web application.
- Build tool: Ant / NetBeans project structure.
- Database: Microsoft SQL Server using `DBScript.sql`.
- Application server: Apache Tomcat or equivalent servlet container.
- Operating system: Windows.
- Browsers: Chrome, Edge, Firefox.
- Network: Localhost and LAN testing; stable internet required for external image links, mail service, and VNPay scenarios.

### Test Data

Use seeded records from `DBScript.sql` and additional controlled test data.

- Roles: `admin`, `owner`, `shipper`, `warehouse_manager`, `customer`.
- Stores: Alpha Sneaker Store, Beta Shoe House.
- Products: Adidas, Vans, Converse sample products.
- Orders and shipping records from sample database.
- Positive and negative input sets for username, password, email, phone, quantity, voucher code, and profile/contact content.

## III. Test Planning

### Test Deliverables

- Test Plan document.
- Requirement Traceability Matrix (RTM).
- Functional test cases and checklists.
- Test data set.
- Defect log.
- Test execution report.
- Test summary report.

### Test Items

- Authentication module.
- Product catalog and search module.
- Cart and checkout module.
- Order and shipping module.
- Feedback and contact module.
- Voucher and wallet/payment module.
- Store, product, category, and stock management module.
- News and home setting module.
- Statistics and role-based administration module.
- Utility classes already covered by unit tests.

### Resource Planning

- Test Lead: prepares plan, reviews cases, monitors progress, approves reports.
- Testers: design and execute test cases, capture evidence, log defects, retest fixes.
- Developers: support unit tests, fix defects, assist with root-cause analysis.
- DBA/System Admin: set up SQL Server database, deployment environment, and test accounts.
- Stakeholders or lecturer/team representative: validate acceptance scenarios.

### Test Schedule

| Phase | Activities | Duration |
|---|---|---|
| Test Planning | Review source code, JSP pages, controllers, database script, current tests; identify scope and risks | 1 day |
| Test Design | Write test scenarios, test cases, RTM, and prepare test data | 2 days |
| Environment Setup | Deploy database and application server; verify mail/payment configuration if available | 1 day |
| Unit Test Review | Execute available model and util tests; record baseline results | 0.5 day |
| Integration Testing | Test controller-DAO-database interactions and role filters | 2 days |
| System Testing | Execute end-to-end customer and staff workflows | 3 days |
| Regression Testing | Re-run impacted test cases after bug fixes | 1 day |
| Test Closure | Summarize results, unresolved issues, lessons learned | 0.5 day |

Estimated total: 11 days.

## IV. Test Design

### Test Scenarios

#### 1. Authentication and Account Management

Goal: Ensure users can access the system securely and manage passwords/accounts correctly.

Scenario:

- Register with valid data.
- Register with duplicate username or invalid email/phone/password.
- Login with valid credentials.
- Login with invalid password.
- Login with inactive account.
- Trigger forgot password and OTP flow.
- Verify OTP with valid and invalid code.
- Reset password and login again.
- Activate account successfully.
- Edit profile information.

#### 2. Product Discovery and Product Detail

Goal: Ensure customers can find and review products efficiently.

Scenario:

- Load home page and featured products.
- Browse by category.
- Search by keyword.
- Open product detail.
- View related feedback/news if shown.
- Verify behavior when product does not exist.

#### 3. Cart and Checkout

Goal: Ensure customers can add products to cart and create orders successfully.

Scenario:

- Add product to cart with valid quantity.
- Update cart quantity.
- Remove item from cart.
- Verify cart expiration behavior.
- Checkout with valid shipping information.
- Checkout with empty cart.
- Checkout with invalid phone/address data.
- Apply voucher if valid.
- Attempt to use expired or invalid voucher.
- Redirect to thank-you page after successful order.

#### 4. Order, Shipping, and Purchase History

Goal: Ensure order lifecycle and shipping assignment/status are correct.

Scenario:

- View created order in order list/history.
- View order detail.
- Display correct shipping information.
- Update shipping status as shipper/admin if authorized.
- Prevent unauthorized role from accessing shipping/order manager pages.

#### 5. Feedback and Contact

Goal: Ensure customers can submit feedback and contact requests, and managers can process them.

Scenario:

- Submit valid feedback.
- Submit invalid rating/content.
- View feedback list on product or manager page.
- Submit contact message for an order.
- Manager responds to contact or feedback.

#### 6. Store and Inventory Management

Goal: Ensure staff roles can manage store-related data safely.

Scenario:

- Add, edit, and delete category.
- Add, edit, and delete product.
- Import stock with valid quantity.
- Reject zero or negative stock import quantity.
- View stock history.
- Manage store assignment information.
- Manage home setting and sliders/news if supported by role.

#### 7. Voucher, Wallet, and Payment

Goal: Ensure discounts and payment workflow behave as expected.

Scenario:

- Create and manage voucher.
- Apply voucher when order value meets minimum.
- Reject expired voucher.
- Start VNPay payment flow.
- Handle payment return response.
- Verify wallet page loads and reflects supported action paths.

#### 8. Administration and Authorization

Goal: Ensure admin-only features are protected and role assignment works correctly.

Scenario:

- Access manager account/category/contact/news/statistic pages as admin.
- Attempt protected page access as customer.
- Verify `AdminFilter` blocks unauthorized users.
- Manage staff/store assignments.

### Representative Test Cases

| Test Case ID | Module | Test Objective | Input / Action | Expected Result |
|---|---|---|---|---|
| TC-LOGIN-01 | Login | Login with valid account | Enter valid username/password | User is authenticated and redirected correctly |
| TC-LOGIN-02 | Login | Reject invalid password | Valid username + wrong password | Error message is shown, session not created |
| TC-SIGNUP-01 | Signup | Register new customer | Valid username, strong password, valid email/phone | Account created successfully |
| TC-RESET-01 | Forgot Password | Reset password with valid OTP | Request OTP, verify OTP, submit new password | Password updated and new login works |
| TC-PROD-01 | Product | Search product by keyword | Search `Adidas` | Matching products are displayed |
| TC-CART-01 | Cart | Add item to cart | Click add-to-cart on valid product | Cart count and content are updated |
| TC-CART-02 | Cart | Update cart quantity | Set quantity to valid positive integer | Cart total is recalculated |
| TC-CHECKOUT-01 | Checkout | Create order successfully | Checkout with valid cart and shipping info | Order, shipping, and order detail records are created |
| TC-CHECKOUT-02 | Checkout | Reject checkout with empty cart | Open checkout without items | User receives validation/error message |
| TC-VOUCHER-01 | Voucher | Apply valid voucher | Enter valid voucher meeting conditions | Discount applied correctly |
| TC-SHIP-01 | Shipping | Update shipping status | Authorized shipper updates status | Status is saved and visible in order/shipping page |
| TC-FEED-01 | Feedback | Submit feedback | Valid rating and content | Feedback saved successfully |
| TC-PROD-MGMT-01 | Product Management | Add product | Fill valid product form | Product stored and visible in manager page |
| TC-STOCK-01 | Stock | Import stock | Valid product, store, positive quantity | Stock import record created |
| TC-AUTHZ-01 | Authorization | Block unauthorized access | Customer opens admin URL | Access denied or redirected |

### Existing Unit Test Baseline

Current automated unit tests in the project indicate coverage for:

- Model constructors, getters/setters, and role logic.
- Validation rules for email, phone, blank values, numeric parsing, password strength, and size list format.
- Password hashing and role helper behavior.
- Cart item counting logic.

These tests should be executed as a baseline before integration and system testing.

## V. Test Execution

### Test Execution Schedule

| Activity | Start Date | End Date | Duration |
|---|---|---|---|
| Test Planning | 23/04/2026 | 23/04/2026 | 1 day |
| Test Design | 24/04/2026 | 25/04/2026 | 2 days |
| Environment Setup | 26/04/2026 | 26/04/2026 | 1 day |
| Unit Test Execution | 27/04/2026 | 27/04/2026 | 0.5 day |
| Integration Testing | 27/04/2026 | 28/04/2026 | 2 days |
| System Testing | 29/04/2026 | 01/05/2026 | 3 days |
| Regression Testing | 02/05/2026 | 02/05/2026 | 1 day |
| Test Reporting and Closure | 03/05/2026 | 03/05/2026 | 0.5 day |

### Test Execution Environment

- Database restored or created from `DBScript.sql`.
- Application deployed successfully to servlet container.
- Browser cache cleared between selected regression runs.
- Valid mail configuration and VNPay sandbox configuration prepared when external integration tests are executed.

### Defect Reporting

Each defect should include:

- Defect ID.
- Summary.
- Module/page/controller.
- Preconditions.
- Steps to reproduce.
- Actual result.
- Expected result.
- Severity and priority.
- Screenshot/log evidence.
- Build/environment information.
- Status and retest result.

Suggested severity levels:

- Critical: system crash, checkout blocked, security bypass, data corruption.
- High: key feature fails, role access control broken, order data incorrect.
- Medium: validation issue, incorrect UI behavior, non-critical calculation defect.
- Low: cosmetic issue, text alignment, non-blocking message mismatch.

## VI. Test Closure

### Criteria for Test Completion

Testing can be closed when all conditions below are met:

- All planned critical and high-priority test cases have been executed.
- No open critical defects remain.
- Open high defects are accepted by stakeholders or fixed and retested.
- Core workflows pass: login, product browsing, cart, checkout, order creation, shipping management, role-based access.
- Unit test baseline has been executed and recorded.
- Test summary report has been prepared and reviewed.

### Test Summary Report Template

| Type of Testing | Number of Test Cases | Number of Defects | Description of Defects Found | Status |
|---|---|---|---|---|
| Unit Testing | X | Y | Model/util logic issues if any | Passed / Failed |
| Integration Testing | X | Y | Controller-DAO-session-db issues | Passed / Failed |
| System Testing | X | Y | End-to-end business flow issues | Passed / Failed |
| GUI Testing | X | Y | Layout/form/display issues | Passed / Failed |
| Security Testing | X | Y | Authentication/authorization/input issues | Passed / Failed |
| Database Testing | X | Y | CRUD/integrity/data consistency issues | Passed / Failed |
| Performance Testing | X | Y | Slow pages/timeouts/load issues | Passed / Failed |

### Common Closure Outputs

- Final defect status list.
- Passed/failed test case statistics.
- Coverage against planned modules.
- Deferred issues and rationale.
- Lessons learned and improvement recommendations.

## VII. Risk and Contingencies

### Identify Risks

#### 1. Environment Configuration Risk

Risk: SQL Server, mail configuration, VNPay sandbox, or servlet container may not be configured correctly.  
Impact: Several integration and system test cases cannot be executed fully.  
Probability: High.

Contingency Plan: Prepare a documented setup checklist, seed database from `DBScript.sql`, and separate external integration cases from core local cases.

#### 2. Role and Authorization Complexity

Risk: The system has multiple roles (`admin`, `owner`, `shipper`, `warehouse_manager`, `customer`) and URL filtering only partially defined in `web.xml`.  
Impact: Unauthorized access or incorrect restriction may occur.  
Probability: High.

Contingency Plan: Prioritize authorization tests early, create dedicated role accounts, and validate both allowed and denied access paths.

#### 3. Data Consistency Across Cart, Order, Shipping, and Inventory

Risk: Errors in controller or DAO integration may create inconsistent records after checkout or stock import.  
Impact: Incorrect totals, missing order details, wrong shipping data, or stock mismatch.  
Probability: Medium to High.

Contingency Plan: Add database verification steps after each critical workflow and retest with seeded and custom data.

#### 4. External Dependency Availability

Risk: Email OTP and VNPay flows depend on external services or sandbox credentials.  
Impact: Password reset and payment scenarios may be partially blocked.  
Probability: Medium.

Contingency Plan: Use mock/sandbox credentials where possible and mark blocked cases clearly when dependency is unavailable.

#### 5. Limited Automated Test Coverage for Controllers/DAO

Risk: Current automated tests focus mainly on models and utility classes.  
Impact: Many business flows depend on manual or semi-manual testing and regressions may be missed.  
Probability: High.

Contingency Plan: Prioritize smoke/regression suites for high-risk end-to-end paths and expand automation later.

## VIII. Approval

Approvals:

- QA/Test Lead
- Project Leader / Team Representative
- Instructor or Stakeholder Representative

## IX. Appendices

### Glossary

| No. | Term | Meaning |
|---|---|---|
| 1 | QA | Quality Assurance |
| 2 | RTM | Requirement Traceability Matrix |
| 3 | OTP | One-Time Password |
| 4 | DAO | Data Access Object |
| 5 | JSP | JavaServer Pages |
| 6 | UAT | User Acceptance Testing |

### References

- Project source code under `src/java`.
- JSP views under `web`.
- Deployment descriptor `web/WEB-INF/web.xml`.
- Database script `DBScript.sql`.
- Existing unit tests under `test/model` and `test/util`.
- Sample test plan format from `Lab_1_Test Plan.docx`.

### Suggested Next Documents

- Detailed Test Case Specification.
- Requirement Traceability Matrix.
- Defect Log Template.
- Test Summary Report.
