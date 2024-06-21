# Grofers App

## Overview
The Grofers App is an e-commerce platform designed to streamline the management of suppliers, products, categories, users, and orders. The application provides a comprehensive set of features to handle everything from user registration and authentication to order placement and product management. Both user-level and admin-level access are supported, with specific capabilities tailored to each role. The app aims to deliver a seamless shopping experience, efficient data management, and robust administrative controls.

## Features

### User Capabilities
- **User Registration and Authentication**
  - Users can register themselves on the platform via a simple sign-up process.
  - Users can log in and receive a JWT for secure access to their accounts.

- **Order Management**
  - Users can place new orders through an intuitive order placement system.
  - Users can add products to their existing orders.
  - Users can update order details, such as the delivery date and product quantities.
  - Users can view their order history to keep track of their past purchases.
  - Users can receive product recommendations based on their order history, enhancing their shopping experience.

- **Cart Management**
  - Users can add items to their cart, making it easy to manage their selections before placing an order.
  - Users can view and modify the contents of their cart.
  - Users can place orders directly from their cart, streamlining the checkout process.

### Admin Capabilities
- **Supplier Management**
  - Admins can add new suppliers to the platform.
  - Admins can update supplier information to keep records accurate and up-to-date.
  - Admins can delete suppliers when necessary.
  - Admins can view all suppliers through a comprehensive supplier management interface.

- **Product Management**
  - Admins can add new products for specific suppliers, ensuring a diverse product catalog.
  - Admins can update product details to reflect changes in price, availability, or specifications.
  - Admins can delete products that are no longer available or relevant.
  - Admins can view all products in the inventory.

- **Category Management**
  - Admins can add new product categories to organize the product catalog effectively.
  - Admins can update category details as needed.
  - Admins can delete categories that are obsolete.
  - Admins can view all product categories to manage the catalog efficiently.

- **User Management**
  - Admins can add new users with admin roles to help manage the platform.
  - Admins can update user information to ensure accurate records.
  - Admins can delete users who are no longer active.
  - Admins can view all users and their roles.

- **Order Management**
  - Admins can delete orders that are invalid or no longer needed.
  - Admins can view all orders placed on the platform.
  - Admins can update order details to correct any discrepancies.

### Additional Features
- **Pagination and Sorting**
  - All GET API endpoints support pagination and sorting, making it easier to navigate and manage large datasets.
  - Users and admins can sort and paginate through products, orders, and other entities for better accessibility and organization.

## Database Structure
The database schema includes the following entities:
- **Suppliers**: Stores information about suppliers, including their names and contact details.
- **Categories**: Stores information about product categories, helping to organize the product catalog.
- **Products**: Stores details of products, including their names, prices, and associations with suppliers and categories.
- **Users**: Stores user information, including their names, email addresses, passwords, and roles (user or admin).
- **Orders**: Stores order information, including order dates, delivery dates, and total amounts, linked to users.
- **Order Details**: Stores detailed information about products in each order, such as product IDs and quantities.
- **Carts**: Stores information about user carts, including creation and update dates and total amounts.
- **Cart Items**: Stores information about items in each cart, such as product IDs and quantities.

## Entity Relationships
<img src="", alt="" />
![e-r-diagram](https://github.com/ramrajpatil/Grofers/assets/170900166/b463162a-a255-452f-bd57-8878ad1356fc)


This structured approach ensures efficient data organization and supports the various functionalities of the Grofers App, facilitating a smooth and comprehensive e-commerce experience for both users and administrators.


