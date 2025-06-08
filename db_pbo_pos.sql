-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: mysql
-- Generation Time: Jun 08, 2025 at 01:51 PM
-- Server version: 8.0.42
-- PHP Version: 8.2.27

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_pbo_pos`
--

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `category_id` int NOT NULL,
  `category_name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `description` text COLLATE utf8mb4_general_ci,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`category_id`, `category_name`, `description`, `created_at`) VALUES
(23, 'Bahan pokok', 'Kategori untuk bahan makanan dan minuman', '2025-06-08 12:06:07'),
(24, 'Kecantikan', 'Kategori untuk produk kecantikan', '2025-06-08 12:06:07');

-- --------------------------------------------------------

--
-- Table structure for table `debt_transactions`
--

CREATE TABLE `debt_transactions` (
  `transaction_id` int NOT NULL,
  `debtor_name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `address` text COLLATE utf8mb4_general_ci NOT NULL,
  `phone_number` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `loan_date` date NOT NULL,
  `due_date` date NOT NULL,
  `amount` decimal(15,2) NOT NULL,
  `status` enum('BELUM_LUNAS','LUNAS','TERLAMBAT','DIBATALKAN') COLLATE utf8mb4_general_ci DEFAULT 'BELUM_LUNAS',
  `created_by` int NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `debt_transactions`
--

INSERT INTO `debt_transactions` (`transaction_id`, `debtor_name`, `address`, `phone_number`, `loan_date`, `due_date`, `amount`, `status`, `created_by`, `created_at`, `updated_at`) VALUES
(8, 'Agus Santoso', 'Jl. Merdeka No. 10', '08123456789', '2025-06-08', '2025-06-15', 1000000.00, 'BELUM_LUNAS', 28, '2025-06-08 12:06:07', '2025-06-08 12:06:07'),
(9, 'Budi Setiawan', 'Jl. Kebangsaan No. 20', '08234567890', '2025-06-08', '2025-06-15', 2000000.00, 'LUNAS', 28, '2025-06-08 12:06:07', '2025-06-08 12:06:07'),
(10, 'Citra Wulandari', 'Jl. Cinta No. 30', '08345678901', '2025-06-08', '2025-06-15', 1500000.00, 'TERLAMBAT', 28, '2025-06-08 12:06:07', '2025-06-08 12:06:07');

-- --------------------------------------------------------

--
-- Table structure for table `log_activities`
--

CREATE TABLE `log_activities` (
  `log_id` int NOT NULL,
  `user_id` int DEFAULT NULL,
  `action` text COLLATE utf8mb4_general_ci NOT NULL,
  `module` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `log_level` enum('DEBUG','INFO','WARNING','ERROR') COLLATE utf8mb4_general_ci DEFAULT 'INFO',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `product_id` int NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `barcode` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `category_id` int NOT NULL,
  `stock` int DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `purchase_price` decimal(12,2) NOT NULL DEFAULT '0.00',
  `selling_price` decimal(12,2) NOT NULL DEFAULT '0.00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`product_id`, `name`, `barcode`, `category_id`, `stock`, `created_at`, `updated_at`, `purchase_price`, `selling_price`) VALUES
(20, 'Beras', '1234567890123', 23, 50, '2025-06-08 12:06:07', '2025-06-08 12:06:07', 40000.00, 50000.00),
(21, 'Sabun Mandi', '9876543210987', 24, 100, '2025-06-08 12:06:07', '2025-06-08 12:06:07', 10000.00, 15000.00),
(22, 'Telur', '1112223334445', 23, 30, '2025-06-08 12:06:07', '2025-06-08 12:06:07', 20000.00, 25000.00);

-- --------------------------------------------------------

--
-- Table structure for table `stock_transactions`
--

CREATE TABLE `stock_transactions` (
  `transaction_id` int NOT NULL,
  `product_id` int NOT NULL,
  `quantity` int NOT NULL,
  `transaction_type` enum('IN','OUT') COLLATE utf8mb4_general_ci NOT NULL,
  `reference_note` text COLLATE utf8mb4_general_ci,
  `performed_by` int NOT NULL,
  `transaction_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int NOT NULL,
  `username` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `is_owner` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `password_hash`, `email`, `is_owner`, `created_at`, `updated_at`) VALUES
(28, 'Owner', 'owner123', 'owner@example.com', 1, '2025-06-08 12:06:07', '2025-06-08 12:06:07'),
(29, 'Staff', 'staff123', 'staff@example.com', 0, '2025-06-08 12:06:07', '2025-06-08 12:06:07'),
(30, 'Stafftwo', 'stafftwo123', 'stafftwo@example.com', 0, '2025-06-08 12:06:07', '2025-06-08 12:06:07');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`category_id`),
  ADD UNIQUE KEY `category_name` (`category_name`);

--
-- Indexes for table `debt_transactions`
--
ALTER TABLE `debt_transactions`
  ADD PRIMARY KEY (`transaction_id`),
  ADD KEY `created_by` (`created_by`),
  ADD KEY `idx_debt_transactions_status` (`status`),
  ADD KEY `idx_debt_transactions_due_date` (`due_date`);

--
-- Indexes for table `log_activities`
--
ALTER TABLE `log_activities`
  ADD PRIMARY KEY (`log_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `idx_log_activities_created_at` (`created_at`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`product_id`),
  ADD UNIQUE KEY `barcode` (`barcode`),
  ADD KEY `category_id` (`category_id`),
  ADD KEY `idx_products_barcode` (`barcode`);

--
-- Indexes for table `stock_transactions`
--
ALTER TABLE `stock_transactions`
  ADD PRIMARY KEY (`transaction_id`),
  ADD KEY `product_id` (`product_id`),
  ADD KEY `performed_by` (`performed_by`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `idx_users_email` (`email`),
  ADD KEY `idx_users_username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `category_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT for table `debt_transactions`
--
ALTER TABLE `debt_transactions`
  MODIFY `transaction_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `log_activities`
--
ALTER TABLE `log_activities`
  MODIFY `log_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=410;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `product_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `stock_transactions`
--
ALTER TABLE `stock_transactions`
  MODIFY `transaction_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `debt_transactions`
--
ALTER TABLE `debt_transactions`
  ADD CONSTRAINT `debt_transactions_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `products_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`);

--
-- Constraints for table `stock_transactions`
--
ALTER TABLE `stock_transactions`
  ADD CONSTRAINT `stock_transactions_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`),
  ADD CONSTRAINT `stock_transactions_ibfk_2` FOREIGN KEY (`performed_by`) REFERENCES `users` (`user_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
