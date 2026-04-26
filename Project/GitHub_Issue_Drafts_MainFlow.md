# GitHub Issue Drafts for Main Flow

This file contains 30 GitHub issue drafts in English based on the main flow integration and system test cases.

## Suggested Labels

- `bug`
- `validation`
- `security`
- `authentication`
- `authorization`
- `session`
- `cart`
- `checkout`
- `voucher`
- `order`
- `shipping`
- `profile`
- `ux`
- `high-priority`

---

## Issue 01

**Title:** Signup allows invalid email format to pass validation

**Description:**  
The signup flow may not reliably reject invalid email formats such as `abcgmail.com`.

**Steps to Reproduce:**
1. Open `/signup`.
2. Enter an invalid email such as `abcgmail.com`.
3. Submit the form.

**Expected Result:**  
The system shows a validation error and does not create a new account.

**Actual Result:**  
The request may continue further than expected or validation feedback may be unclear.

**Labels:** `bug`, `validation`, `authentication`

**Priority:** Medium

---

## Issue 02

**Title:** Weak password is not blocked consistently during signup

**Description:**  
Weak passwords such as `abc123` may not be rejected consistently by the signup validation rules.

**Steps to Reproduce:**
1. Open `/signup`.
2. Enter a weak password such as `abc123`.
3. Submit the form.

**Expected Result:**  
The system rejects the password, shows a clear validation message, and does not create the account.

**Actual Result:**  
The system may accept the password or return unclear feedback.

**Labels:** `bug`, `validation`, `authentication`, `high-priority`

**Priority:** High

---

## Issue 03

**Title:** Signup does not preserve username and email after validation error

**Description:**  
When signup fails because of validation errors, previously entered values such as username and email may be cleared from the form.

**Steps to Reproduce:**
1. Open `/signup`.
2. Enter a valid username and email.
3. Enter an invalid password.
4. Submit the form.

**Expected Result:**  
The form keeps the valid username and email values so the user does not need to re-enter them.

**Actual Result:**  
The user may have to fill in the fields again.

**Labels:** `bug`, `ux`, `validation`

**Priority:** Medium

---

## Issue 04

**Title:** Duplicate username check may not prevent account creation reliably

**Description:**  
The duplicate username validation may not fully stop the account creation flow before insert logic is reached.

**Steps to Reproduce:**
1. Prepare an existing account with a known username.
2. Try to sign up with the same username.
3. Submit the form.

**Expected Result:**  
The system shows a duplicate username error and does not insert a new account.

**Actual Result:**  
The request may still proceed too far in the flow or behave inconsistently.

**Labels:** `bug`, `validation`, `authentication`, `high-priority`

**Priority:** High

---

## Issue 05

**Title:** Duplicate email signup flow may still reach insert logic

**Description:**  
An email address that already exists may not be blocked early enough in the signup flow.

**Steps to Reproduce:**
1. Prepare an existing account with a known email address.
2. Try to sign up with the same email.
3. Submit the form.

**Expected Result:**  
The system shows a duplicate email error and does not create a new account.

**Actual Result:**  
The request may still progress into later processing unexpectedly.

**Labels:** `bug`, `validation`, `authentication`, `high-priority`

**Priority:** High

---

## Issue 06

**Title:** Invalid activation token is not handled with clear user feedback

**Description:**  
The account activation flow may fail silently or provide unclear feedback when the activation token is invalid.

**Steps to Reproduce:**
1. Open an activation URL with an invalid token.
2. Observe the response.

**Expected Result:**  
The account remains inactive and the user sees a clear error message.

**Actual Result:**  
The flow may fail without useful feedback.

**Labels:** `bug`, `authentication`, `ux`

**Priority:** Medium

---

## Issue 07

**Title:** Inactive account can still attempt login without proper blocking message

**Description:**  
The login flow for inactive accounts may not clearly block authentication or explain the reason to the user.

**Steps to Reproduce:**
1. Use a valid but inactive account.
2. Log in with correct credentials.

**Expected Result:**  
No authenticated session is created and the user sees an inactive-account message.

**Actual Result:**  
The feedback may be unclear or session handling may not be strict enough.

**Labels:** `bug`, `authentication`, `session`

**Priority:** High

---

## Issue 08

**Title:** Remember username cookie is not cleared when checkbox is unchecked

**Description:**  
The remember-username cookie may remain in the browser even after the user logs in without selecting the remember option.

**Steps to Reproduce:**
1. Log in once with the remember option enabled.
2. Log out.
3. Log in again without selecting remember.
4. Inspect the browser cookies.

**Expected Result:**  
The `userC` cookie is cleared.

**Actual Result:**  
The old cookie may still remain.

**Labels:** `bug`, `session`, `ux`

**Priority:** Medium

---

## Issue 09

**Title:** Invalid product detail ID may trigger server error instead of safe fallback

**Description:**  
Opening a product detail page with an invalid or non-existing product ID may result in a server-side error.

**Steps to Reproduce:**
1. Open `/detail?pid=999999`.
2. Observe the response.

**Expected Result:**  
The system redirects safely or shows an empty-safe state without returning a server error.

**Actual Result:**  
The page may fail unexpectedly.

**Labels:** `bug`, `validation`

**Priority:** High

---

## Issue 10

**Title:** Search results may return unrelated products for keyword queries

**Description:**  
Keyword search may return products that do not actually match the entered term.

**Steps to Reproduce:**
1. Search using a keyword such as `Adidas`.
2. Review the returned product list.

**Expected Result:**  
Only matching products are returned.

**Actual Result:**  
Unrelated items may appear in the results.

**Labels:** `bug`

**Priority:** Medium

---

## Issue 11

**Title:** Category filter may mix products from other categories

**Description:**  
Filtering by category may show products that belong to other categories.

**Steps to Reproduce:**
1. Select a specific category.
2. Inspect the returned product list.

**Expected Result:**  
Only products from the selected category are displayed.

**Actual Result:**  
Products from unrelated categories may also be listed.

**Labels:** `bug`

**Priority:** Medium

---

## Issue 12

**Title:** Non-customer users can still access add-to-cart endpoint

**Description:**  
Users with non-customer roles such as owner or admin may still be able to access the add-to-cart flow.

**Steps to Reproduce:**
1. Log in with a non-customer account.
2. Open `/add-to-cart?productId=x`.

**Expected Result:**  
The request is blocked, the user is redirected safely, and the cart remains unchanged.

**Actual Result:**  
The action may still proceed or behave incorrectly.

**Labels:** `bug`, `authorization`, `cart`, `high-priority`

**Priority:** High

---

## Issue 13

**Title:** Invalid productId in add-to-cart is not validated safely

**Description:**  
An invalid `productId` value such as `abc` may cause an exception or unstable flow in the add-to-cart endpoint.

**Steps to Reproduce:**
1. Log in as a customer.
2. Open `/add-to-cart?productId=abc`.

**Expected Result:**  
The system shows a warning, does not crash, and does not add any item to the cart.

**Actual Result:**  
The request may trigger an error or unsafe behavior.

**Labels:** `bug`, `validation`, `cart`

**Priority:** High

---

## Issue 14

**Title:** Out-of-stock products can still be added into cart

**Description:**  
Products with zero available quantity may still be added into the cart.

**Steps to Reproduce:**
1. Choose a product with stock quantity equal to `0`.
2. Attempt to add it to the cart.

**Expected Result:**  
The item is not added and the user sees an out-of-stock message.

**Actual Result:**  
The product may still appear in the cart.

**Labels:** `bug`, `cart`, `checkout`, `high-priority`

**Priority:** High

---

## Issue 15

**Title:** Cart quantity update accepts non-numeric input

**Description:**  
The cart quantity update flow may accept invalid values such as `abc`.

**Steps to Reproduce:**
1. Open the cart page.
2. Enter `abc` as the quantity.
3. Submit the update.

**Expected Result:**  
The system shows an invalid quantity message and keeps the cart unchanged.

**Actual Result:**  
The request may fail unexpectedly or alter the cart incorrectly.

**Labels:** `bug`, `validation`, `cart`

**Priority:** Medium

---

## Issue 16

**Title:** Cart allows quantity increase beyond available stock

**Description:**  
Customers may be able to increase item quantity beyond the available stock.

**Steps to Reproduce:**
1. Add a low-stock product to the cart.
2. Increase the quantity beyond the available inventory.
3. Submit the update.

**Expected Result:**  
The system rejects the update and shows an insufficient stock message.

**Actual Result:**  
The cart may still be updated beyond the allowed limit.

**Labels:** `bug`, `cart`, `checkout`, `high-priority`

**Priority:** High

---

## Issue 17

**Title:** Expired cart items are not released correctly before checkout

**Description:**  
Reserved items that have expired may still remain in the session cart or may not release stock correctly.

**Steps to Reproduce:**
1. Add an item to the cart.
2. Wait for the expiration timeout or trigger the expiration logic.
3. Open the cart or checkout flow.

**Expected Result:**  
Expired items are removed and reserved stock is released.

**Actual Result:**  
Expired items may still remain visible or continue to block stock.

**Labels:** `bug`, `cart`, `session`, `checkout`, `high-priority`

**Priority:** High

---

## Issue 18

**Title:** Checkout can proceed with empty cart

**Description:**  
The checkout flow may still be accessible or submittable when the cart is empty.

**Steps to Reproduce:**
1. Log in as a customer.
2. Make sure the cart is empty.
3. Open or submit the checkout flow.

**Expected Result:**  
The system redirects the user back to the cart and shows an empty-cart message.

**Actual Result:**  
The flow may continue when it should not.

**Labels:** `bug`, `checkout`, `high-priority`

**Priority:** High

---

## Issue 19

**Title:** Checkout form does not preserve entered data after validation failure

**Description:**  
When checkout validation fails, previously entered receiver information may be lost.

**Steps to Reproduce:**
1. Enter a valid name and address on the checkout page.
2. Enter an invalid phone number.
3. Submit the form.

**Expected Result:**  
The form reloads with the previously entered valid values preserved.

**Actual Result:**  
The user may need to re-enter the full form.

**Labels:** `bug`, `ux`, `validation`, `checkout`

**Priority:** Medium

---

## Issue 20

**Title:** Invalid phone format is not consistently rejected during checkout

**Description:**  
Phone values such as `12345abc` may not be rejected consistently in the checkout validation flow.

**Steps to Reproduce:**
1. Open the checkout page.
2. Enter an invalid phone number such as `12345abc`.
3. Submit the form.

**Expected Result:**  
The system shows a validation error and does not create the order.

**Actual Result:**  
The request may continue or return unclear validation feedback.

**Labels:** `bug`, `validation`, `checkout`

**Priority:** High

---

## Issue 21

**Title:** Voucher under minimum order value may still be applied

**Description:**  
The system may apply a voucher discount even when the order subtotal does not meet the voucher minimum value.

**Steps to Reproduce:**
1. Prepare a voucher with a minimum order requirement.
2. Start checkout with a subtotal below that threshold.
3. Apply the voucher and submit.

**Expected Result:**  
The voucher is ignored and no discount is applied.

**Actual Result:**  
The checkout total may still be reduced incorrectly.

**Labels:** `bug`, `voucher`, `checkout`, `high-priority`

**Priority:** High

---

## Issue 22

**Title:** Invalid voucher code may break checkout flow

**Description:**  
Using a voucher code that does not exist may interrupt the checkout flow or cause an order creation failure.

**Steps to Reproduce:**
1. Open the checkout page.
2. Enter an invalid voucher code such as `NOPE123`.
3. Submit the checkout form.

**Expected Result:**  
The order is still created normally, but no discount is applied.

**Actual Result:**  
The flow may fail or behave unexpectedly.

**Labels:** `bug`, `voucher`, `checkout`

**Priority:** High

---

## Issue 23

**Title:** Multi-store checkout may not split orders correctly

**Description:**  
When the cart contains products from multiple stores, the checkout flow may not create separate orders and shipping records correctly.

**Steps to Reproduce:**
1. Add products from two different stores into the cart.
2. Proceed to checkout.
3. Complete the order and inspect the resulting records.

**Expected Result:**  
The system creates one order and one shipping record per store.

**Actual Result:**  
The data may be grouped incorrectly or mapped to the wrong store.

**Labels:** `bug`, `checkout`, `order`, `high-priority`

**Priority:** High

---

## Issue 24

**Title:** Cart session is not cleared after successful checkout

**Description:**  
The session cart may still remain after an order has been completed successfully.

**Steps to Reproduce:**
1. Complete a successful checkout.
2. Open the cart page again.

**Expected Result:**  
The cart is empty and the cart session data has been removed.

**Actual Result:**  
Old cart data may still be present.

**Labels:** `bug`, `session`, `cart`, `checkout`

**Priority:** High

---

## Issue 25

**Title:** Refreshing thank-you page may create duplicate orders

**Description:**  
Refreshing the thank-you page after a completed checkout may create a duplicate order.

**Steps to Reproduce:**
1. Complete a successful checkout.
2. Refresh the thank-you page.
3. Check purchase history or database records.

**Expected Result:**  
No additional order is created.

**Actual Result:**  
Duplicate orders may appear.

**Labels:** `bug`, `checkout`, `order`, `high-priority`

**Priority:** High

---

## Issue 26

**Title:** Purchase history is accessible without authentication

**Description:**  
Unauthenticated users may be able to access the purchase history page.

**Steps to Reproduce:**
1. Log out or open a new browser session.
2. Navigate to `/purchaseHistory`.

**Expected Result:**  
The user is redirected to the login page.

**Actual Result:**  
The page may still be accessible or may not follow the correct auth flow.

**Labels:** `bug`, `authentication`, `authorization`, `high-priority`

**Priority:** High

---

## Issue 27

**Title:** Customer can access owner or shipper order management endpoints

**Description:**  
A customer account may still be able to access restricted endpoints such as `/orders` or `/shipping`.

**Steps to Reproduce:**
1. Log in as a customer.
2. Open `/orders`.
3. Open `/shipping?orderId=x`.

**Expected Result:**  
Access is denied and the user is redirected safely.

**Actual Result:**  
Restricted pages or data may still be reachable.

**Labels:** `bug`, `authorization`, `order`, `shipping`, `high-priority`

**Priority:** High

---

## Issue 28

**Title:** Owner can assign invalid account as shipper to an order

**Description:**  
The shipper assignment flow may accept an account that is not actually a shipper.

**Steps to Reproduce:**
1. Log in as an owner.
2. Choose an order.
3. Assign a non-shipper account.

**Expected Result:**  
The system rejects the assignment and keeps the current data unchanged.

**Actual Result:**  
An invalid account may be assigned as the shipper.

**Labels:** `bug`, `authorization`, `order`, `shipping`

**Priority:** High

---

## Issue 29

**Title:** Shipper can view or update shipping of unassigned orders

**Description:**  
A shipper may be able to view or update shipping records that are not assigned to them.

**Steps to Reproduce:**
1. Log in as shipper A.
2. Open a shipping detail page for an order assigned to shipper B or not assigned at all.
3. Attempt to update the status.

**Expected Result:**  
Access is blocked and no update is allowed.

**Actual Result:**  
The shipper may still be able to view or modify restricted shipping data.

**Labels:** `bug`, `authorization`, `shipping`, `high-priority`

**Priority:** High

---

## Issue 30

**Title:** Stored XSS risk in contact and feedback forms

**Description:**  
User input containing HTML or script tags in contact or feedback forms may be stored and rendered without proper escaping.

**Steps to Reproduce:**
1. Submit a contact or feedback message containing `<script>alert(1)</script>`.
2. Open a page that displays the submitted content.

**Expected Result:**  
The content is escaped or sanitized and no script is executed.

**Actual Result:**  
The application may be vulnerable to stored XSS.

**Labels:** `bug`, `security`, `high-priority`

**Priority:** High
