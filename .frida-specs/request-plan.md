<tasks>
  <task>
    <task_name>Refactor Product Management Infrastructure</task_name>
    <subtasks>
      <subtask>
        <id>19</id>
        <name>Create product model and mock data</name>
        <description>Create a dedicated Product interface file in src/app/models/ and a comprehensive mock data file with 10+ realistic product entries covering different departments and varied data.</description>
        <completed>true</completed>
      </subtask>
      <subtask>
        <id>20</id>
        <name>Implement ProductService with reactive data</name>
        <description>Create a ProductService in src/app/services/ that provides getProducts(): Observable<Product[]> method returning mock data through RxJS operators, following Angular best practices.</description>
        <completed>true</completed>
      </subtask>
      <subtask>
        <id>21</id>
        <name>Refactor ProductListComponent to use service</name>
        <description>Update the existing ProductListComponent to implement OnInit, inject ProductService, and reactively load products from the service instead of using hardcoded array data.</description>
        <completed>true</completed>
      </subtask>
      <subtask>
        <id>22</id>
        <name>Add refresh functionality and improve template</name>
        <description>Add an "Actualizar" button that reloads data from the service, optimize the template to display only key fields (nombre, marca, precio, departamento), and ensure proper reactive data binding.</description>
        <completed>true</completed>
      </subtask>
    </subtasks>
  </task>
  
  <task>
    <task_name>File Upload and Product Creation Logic</task_name>
    <subtasks>
      <subtask>
        <id>23</id>
        <name>Enhance ProductService with state management</name>
        <description>Extend ProductService to include addProduct() method, maintain internal state using BehaviorSubject, and implement proper reactive updates for the product list with type safety.</description>
        <completed>true</completed>
      </subtask>
      <subtask>
        <id>24</id>
        <name>Implement file processing and product creation</name>
        <description>Create logic to process uploaded files, extract available data, and generate Product objects with proper field mapping. Set unavailable fields to "--" as specified.</description>
        <completed>true</completed>
      </subtask>
      <subtask>
        <id>25</id>
        <name>Build Save button functionality</name>
        <description>Implement the complete Save button logic that validates uploaded files, processes them into Product objects, adds them to the service, and triggers reactive updates.</description>
        <completed>true</completed>
      </subtask>
      <subtask>
        <id>26</id>
        <name>Add navigation and UI updates</name>
        <description>Implement automatic navigation to ProductListComponent after successful save, ensure counter updates, and maintain proper loading states throughout the process.</description>
        <completed>true</completed>
      </subtask>
    </subtasks>
  </task>

  <task>
    <task_name>Enhance Product Table Display</task_name>
    <subtasks>
      <subtask>
        <id>27</id>
        <name>Add missing columns to product table</name>
        <description>Update the displayedColumns array in ProductListComponent to include 'referencia', 'descripcion', and 'numeroDisponible' columns in their natural order and modify the corresponding template to display these fields.</description>
        <completed>true</completed>
      </subtask>
    </subtasks>
  </task>

  <task>
    <task_name>Implement Product Deletion Functionality</task_name>
    <subtasks>
      <subtask>
        <id>28</id>
        <name>Add delete method to ProductService</name>
        <description>Extend ProductService with a deleteProduct(product: Product) method that removes the product from the internal BehaviorSubject state and updates all subscribers reactively.</description>
        <completed>true</completed>
      </subtask>
      <subtask>
        <id>29</id>
        <name>Implement delete functionality in ProductListComponent</name>
        <description>Add a deleteProduct method to ProductListComponent that calls the service delete method and connect the delete button in the template to trigger this functionality with the correct product reference.</description>
        <completed>true</completed>
      </subtask>
      <subtask>
        <id>30</id>
        <name>Ensure proper UI updates after deletion</name>
        <description>Verify that after deleting a product, the table updates automatically, the product count reflects the new total, and the dataSource is properly refreshed through reactive data binding.</description>
        <completed>true</completed>
      </subtask>
    </subtasks>
  </task>

  <task>
    <task_name>Implement Product Edit Functionality</task_name>
    <subtasks>
      <subtask>
        <id>31</id>
        <name>Create EditProductModalComponent</name>
        <description>Create a reusable modal component with reactive forms for editing products. Include all product fields with proper validation, and provide save/cancel functionality using Angular Material Dialog.</description>
        <completed>true</completed>
      </subtask>
      <subtask>
        <id>32</id>
        <name>Add update method to ProductService</name>
        <description>Extend ProductService with an updateProduct(product: Product) method that modifies the existing product in the BehaviorSubject state and triggers reactive updates to all subscribers.</description>
        <completed>true</completed>
      </subtask>
      <subtask>
        <id>33</id>
        <name>Integrate edit functionality in ProductListComponent</name>
        <description>Add editProduct method to ProductListComponent that opens the modal with selected product data, handles the save response, and connects the edit button in the template to trigger this functionality.</description>
        <completed>true</completed>
      </subtask>
      <subtask>
        <id>34</id>
        <name>Ensure proper data flow and UI updates</name>
        <description>Verify that editing a product updates the table immediately, maintains reactive data binding, preserves the product count, and handles all edge cases like validation errors and modal cancellation.</description>
        <completed>true</completed>
      </subtask>
    </subtasks>
  </task>

  <task>
    <task_name>Implement Product Search and Filtering</task_name>
    <subtasks>
      <subtask>
        <id>35</id>
        <name>Add search functionality to ProductListComponent</name>
        <description>Implement real-time search filtering that responds to the searchText input field. Filter products by nombre, marca, referencia, descripcion, and departamento fields, updating the table and product count dynamically.</description>
        <completed>true</completed>
      </subtask>
      <subtask>
        <id>36</id>
        <name>Optimize search performance and user experience</name>
        <description>Add debouncing to prevent excessive filtering operations, implement case-insensitive search, handle special characters, and ensure the filtered results update the dataSource and totalProducts counter properly.</description>
        <completed>true</completed>
      </subtask>
      <subtask>
        <id>37</id>
        <name>Add search result indicators and clear functionality</name>
        <description>Display appropriate feedback when no results are found, show filtered vs total count when search is active, and optionally add a clear button to reset the search field and show all products.</description>
        <completed>true</completed>
      </subtask>
    </subtasks>
  </task>
</tasks>