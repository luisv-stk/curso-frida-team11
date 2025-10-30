# Requirements Specification

## 1. Overview  
This document specifies the requirements for the Product Management Angular service (“ProductService”) and its associated features, including the new file‐analysis endpoint that must accept multipart form‐data rather than raw binary. The service supports listing, adding, updating, deleting products and uploading files for analysis. It exposes a reactive API to Angular components and communicates with a RESTful backend.

---

## 2. Functional Requirements

### 2.1 Core Functionality  
1. **List Products** (Must Have)  
   - Expose `getProducts(): Observable<Product[]>` that streams the current product list.  

2. **Add Product** (Must Have)  
   - Method `addProduct(product: Product): void` to append a new product.  
   - Trigger reactive updates so subscribed components refresh automatically.

3. **Update Product** (Must Have)  
   - Method `updateProduct(updatedProduct: Product): void` to replace an existing product matching `referencia`.  

4. **Delete Product** (Must Have)  
   - Method `deleteProduct(product: Product): void` to remove the product by `referencia`.

5. **Get Product Count** (Should Have)  
   - Method `getProductCount(): number` returns the current array length.

6. **Analyze File Upload** (Must Have)  
   - Method `analyzeFile(file: File): Observable<any>` sends the file as `multipart/form‐data` to `<apiUrl>/analyze`.  
   - Ensure correct HTTP headers so backend recognizes the multipart boundary.

### 2.2 User Interactions  
- Components subscribe to `products$` (BehaviorSubject) to render a live-updating product list.  
- Add/update/delete operations invoked via UI controls call the corresponding service methods.  
- File‐upload UI control (e.g. `<input type="file">`) passes the selected file object to `analyzeFile()`.  
- Service returns an `Observable` for analyze response; component displays results or errors.

### 2.3 Data Management  
- In-memory store: `BehaviorSubject<Product[]>` initialized with `MOCK_PRODUCTS`.  
- No persistence on the client; for production, integration with remote persistence is assumed.  
- Product model fields (e.g. `referencia`, `nombre`, `precio`, etc.) must be defined in `Product` interface.  
- All HTTP interactions use `HttpClient` and environment-based `apiUrl`.

---

## 3. Non-Functional Requirements

### 3.1 Performance  
- **Response Time**:  
  - `getProducts()` calls should return within 50 ms (in-memory).  
  - File upload round-trip should complete < 2 s for files ≤ 5 MB.  
- **Scalability**:  
  - Service must handle up to 100 concurrent component subscriptions.  
  - Backend calls must not degrade UI responsiveness.

### 3.2 Security  
- **Authentication & Authorization**:  
  - HTTP headers include JWT token (via interceptor) on all requests.  
  - Only authenticated users can call analyze endpoint and modify products.
- **Data Protection**:  
  - File content sent only over HTTPS.  
  - No sensitive product data stored in localStorage.
- **Privacy**:  
  - Uploaded files deleted by backend after analysis.  
  - User must consent to file processing.

### 3.3 Usability  
- **User Experience**:  
  - Adding/updating/deleting products should show immediate UI feedback (loading spinners, toasts).  
- **Accessibility**:  
  - Follow WCAG 2.1 AA for form controls, error messages, and status notifications.  
- **Compatibility**:  
  - Support latest two versions of Chrome, Firefox, Edge, Safari.  
  - Responsive layout when used in mobile/desktop.

### 3.4 Reliability  
- **Availability**:  
  - Service must be available 99.5% of business hours.  
- **Error Handling**:  
  - Gracefully handle HTTP 4xx/5xx errors.  
  - Expose error Observables for components to display messages.  
- **Backup & Recovery**:  
  - Not applicable on client; assume backend handles data durability.

---

## 4. User Stories

1. As an **Admin**, I want to see the current list of products so that I can manage inventory.  
2. As an **Admin**, I want to add a new product so that the catalog stays up to date.  
3. As an **Admin**, I want to edit an existing product so that product details remain accurate.  
4. As an **Admin**, I want to delete a product so that outdated items are removed.  
5. As a **Data Scientist**, I want to upload a CSV file so that it is analyzed in the backend.  
6. As a **User**, I want to see a clear error message if file upload fails so that I know what to do next.  
7. As a **Developer**, I want the service to use multipart/form-data for file uploads so that the backend can parse files correctly.  
8. As a **User**, I want real-time updates in the UI when products change so that I don’t have to refresh.  
9. As a **QA Engineer**, I want deterministic mock data so I can write repeatable tests.  
10. As a **User**, I want the system to block unauthorized access to product management methods.

---

## 5. Constraints and Assumptions

### 5.1 Technical Constraints  
- Must use Angular ≥ 12 and RxJS ≥ 6.  
- HTTP requests via Angular `HttpClient`.  
- Environment variables managed in `environments/*.ts`.  
- Assume a CORS‐enabled backend at `environment.apiUrl`.  
- No direct file parsing on client; only FormData pass-through.

### 5.2 Business Constraints  
- **Timeline**: Delivery in 4 sprints (8 weeks).  
- **Budget**: 1 front-end developer, 0.5 QA resource.  
- **Resources**: Use existing Angular CLI, GitLab CI for pipeline.

### 5.3 Assumptions  
- Backend analyze endpoint supports multipart/form-data under field name `file`.  
- Users have modern browsers with FormData support.  
- Network connection is reliable; small packet loss assumed.  
- Mock data sufficient for initial development; real data will come later.

---

## 6. Acceptance Criteria

- [ ] getProducts() returns initial mock array within 20 ms.  
- [ ] addProduct(), updateProduct(), deleteProduct() reflect changes in `products$` and UI updates without manual refresh.  
- [ ] analyzeFile(file) sends a `POST` to `${apiUrl}/analyze` with header `Content-Type: multipart/form-data; boundary=...`.  
- [ ] Backend receives file under `request.files['file']`.  
- [ ] All HTTP errors are captured; service emits an error observable and components show a user‐friendly message.  
- [ ] Unit tests cover 80% of methods with mocks for HttpClient.  
- [ ] Accessibility audit passes WCAG 2.1 AA.  
- [ ] Browser compatibility tests pass on latest Chrome, Firefox, Edge, Safari.

---

## 7. Out of Scope

- Server-side implementation of `/analyze` endpoint logic.  
- Persistent database integration (beyond in-memory or mock).  
- UI component styling/theme details (assume Material/Bootstrap).  
- Advanced file parsing in client (validation beyond basic file type/size).  
- Analytics/dashboard beyond product list and file‐analysis result display.