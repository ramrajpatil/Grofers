# Grofers

## User Entity

Represents users of the system who can manage products and place orders.

### Attributes:
- `userId`: Unique identifier for the user.
- `name`: User's name.
- `email`: User's email address (unique).
- `password`: User's password.
- `role`: User's role in the system (`UserRole` enum).

### Relationships:
- **OneToMany with Product:** Each user can manage multiple products.
- **OneToMany with Order:** Each user can place multiple orders.

## Supplier Entity

Represents suppliers who provide products to the system.

### Attributes:
- `supplierId`: Unique identifier for the supplier.
- `name`: Supplier's name.
- `email`: Supplier's email address (unique).

### Relationships:
- No explicit relationships defined in the provided code snippet.

## Category Entity

Represents product categories.

### Attributes:
- `categoryId`: Unique identifier for the category.
- `name`: Category name (unique).

### Relationships:
- **OneToMany with Product:** Each category can contain multiple products.

## Order Entity

Represents an order placed by a user, containing products.

### Attributes:
- `orderId`: Unique identifier for the order.
- `orderDate`: Date when the order was placed.
- `deliveryDate`: Expected delivery date.
- `totalAmount`: Total amount for the order.

### Relationships:
- **ManyToOne with User:** Each order belongs to one user.
- **OneToMany with Product:** Each order can contain multiple products.

## Product Entity

Represents products available in the system.

### Attributes:
- `productId`: Unique identifier for the product.
- `name`: Product name.
- `price`: Product price.
- `quantity`: Product quantity available.

### Relationships:
- **ManyToOne with Category:** Each product belongs to one category.
- **ManyToOne with User:** Each product is managed by one user (seller).
- **ManyToOne with Order:** Each product can be associated with one order (if ordered).
