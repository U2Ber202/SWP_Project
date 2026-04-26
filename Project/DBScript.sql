USE [master];
GO

IF DB_ID(N'PRJ301') IS NOT NULL
BEGIN
    ALTER DATABASE [PRJ301] SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE [PRJ301];
END
GO

CREATE DATABASE [PRJ301];
GO

USE [PRJ301];
GO

CREATE TABLE [dbo].[Role] (
    [role_key] VARCHAR(20) NOT NULL,
    [role_name] NVARCHAR(100) NOT NULL,
    [description] NVARCHAR(255) NULL,
    CONSTRAINT [PK_Role] PRIMARY KEY CLUSTERED ([role_key] ASC)
);
GO

INSERT INTO [dbo].[Role] ([role_key], [role_name], [description]) VALUES
('admin', N'Quản trị', N'Toàn quyền quản trị hệ thống'),
('owner', N'Chủ cửa hàng', N'Quản lý một cửa hàng'),
('shipper', N'Quản lý ship hàng', N'Quản lý và cập nhật trạng thái giao hàng'),
('warehouse_manager', N'Quản lý kho', N'Nhập kho theo size và cập nhật tồn kho cho cửa hàng'),
('customer', N'Khách hàng', N'Tài khoản mua hàng thông thường');
GO

CREATE TABLE [dbo].[Account] (
    [uID] INT IDENTITY(1,1) NOT NULL,
    [user] VARCHAR(255) NOT NULL,
    [pass] VARCHAR(255) NOT NULL,
    [isAdmin] INT NOT NULL DEFAULT 0,
    [role] VARCHAR(20) NOT NULL DEFAULT 'customer',
    [active] BIT NOT NULL DEFAULT 1,
    [fullname] NVARCHAR(255) NULL,
    [phone] NVARCHAR(50) NULL,
    [email] NVARCHAR(255) NULL,
    [address] NVARCHAR(255) NULL,
    [token] VARCHAR(255) NULL,

    CONSTRAINT [PK_Account] PRIMARY KEY CLUSTERED ([uID] ASC),
    CONSTRAINT [UQ_Account_user] UNIQUE ([user]),
    CONSTRAINT [FK_Account_Role] FOREIGN KEY ([role]) REFERENCES [dbo].[Role]([role_key])
);
GO

CREATE TABLE [dbo].[Store] (
    [store_id] INT IDENTITY(1,1) NOT NULL,
    [store_name] NVARCHAR(255) NOT NULL,
    [owner_id] INT NOT NULL,
    [shipper_id] INT NULL,
    [warehouse_manager_id] INT NULL,
    [active] BIT NOT NULL DEFAULT 1,
    CONSTRAINT [PK_Store] PRIMARY KEY CLUSTERED ([store_id] ASC),
    CONSTRAINT [UQ_Store_owner_id] UNIQUE ([owner_id]),
    CONSTRAINT [UQ_Store_shipper_id] UNIQUE ([shipper_id]),
    CONSTRAINT [UQ_Store_warehouse_manager_id] UNIQUE ([warehouse_manager_id]),
    CONSTRAINT [FK_Store_Account] FOREIGN KEY ([owner_id]) REFERENCES [dbo].[Account]([uID]),
    CONSTRAINT [FK_Store_Shipper] FOREIGN KEY ([shipper_id]) REFERENCES [dbo].[Account]([uID]),
    CONSTRAINT [FK_Store_WarehouseManager] FOREIGN KEY ([warehouse_manager_id]) REFERENCES [dbo].[Account]([uID])
);
GO

CREATE TABLE [dbo].[Category] (
    [cid] INT IDENTITY(1,1) NOT NULL,
    [cname] NVARCHAR(50) NOT NULL,
    [store_id] INT NULL,
    CONSTRAINT [PK_Category] PRIMARY KEY CLUSTERED ([cid] ASC),
    CONSTRAINT [FK_Category_Store] FOREIGN KEY ([store_id]) REFERENCES [dbo].[Store]([store_id]) ON DELETE CASCADE
);
GO

IF COL_LENGTH('dbo.Category', 'manufacturer') IS NULL
BEGIN
    ALTER TABLE [dbo].[Category]
    ADD [manufacturer] NVARCHAR(100) NULL;
END
GO

UPDATE [dbo].[Category]
SET [manufacturer] = ISNULL([manufacturer], N'Vietnam');
GO

CREATE TABLE [dbo].[Product] (
    [id] INT IDENTITY(1,1) NOT NULL,
    [name] NVARCHAR(MAX) NULL,
    [image] NVARCHAR(MAX) NULL,
    [price] INT NULL,
    [title] NVARCHAR(MAX) NULL,
    [description] NVARCHAR(MAX) NULL,
    [cateID] INT NULL,
    [quantity] INT NOT NULL DEFAULT 0,
    [sell_ID] INT NULL,
    [store_id] INT NULL,
    CONSTRAINT [PK_Product] PRIMARY KEY CLUSTERED ([id] ASC),
    CONSTRAINT [FK_Product_Category] FOREIGN KEY ([cateID]) REFERENCES [dbo].[Category]([cid]),
    CONSTRAINT [FK_Product_Store] FOREIGN KEY ([store_id]) REFERENCES [dbo].[Store]([store_id]) ON DELETE CASCADE
);
GO

IF COL_LENGTH('dbo.Product', 'manufacturer') IS NULL
BEGIN
    ALTER TABLE [dbo].[Product]
    ADD [manufacturer] NVARCHAR(100) NULL;
END
GO

UPDATE [dbo].[Product]
SET [manufacturer] = ISNULL([manufacturer], N'Vietnam');
GO

CREATE TABLE [dbo].[Cart] (
    [AccountID] INT NOT NULL,
    [ProductID] INT NOT NULL,
    [Amount] INT NULL,
    [reserved_at] DATETIME NULL,
    [expires_at] DATETIME NULL,
    CONSTRAINT [PK_Cart] PRIMARY KEY CLUSTERED ([AccountID] ASC, [ProductID] ASC),
    CONSTRAINT [FK_Cart_Account] FOREIGN KEY ([AccountID]) REFERENCES [dbo].[Account]([uID]),
    CONSTRAINT [FK_Cart_Product] FOREIGN KEY ([ProductID]) REFERENCES [dbo].[Product]([id])
);
GO

CREATE TABLE [dbo].[Shipping] (
    [id] INT IDENTITY(1,1) NOT NULL,
    [name] NVARCHAR(255) NULL,
    [phone] NVARCHAR(255) NULL,
    [address] NVARCHAR(255) NULL,
    [Status] VARCHAR(50) NULL,
    [shipper_id] INT NULL,
    [store_id] INT NULL,
    [shipped_date] DATETIME NULL,
    CONSTRAINT [PK_Shipping] PRIMARY KEY CLUSTERED ([id] ASC),
    CONSTRAINT [FK_Shipping_Account] FOREIGN KEY ([shipper_id]) REFERENCES [dbo].[Account]([uID]),
    CONSTRAINT [FK_Shipping_Store] FOREIGN KEY ([store_id]) REFERENCES [dbo].[Store]([store_id])
);
GO

CREATE TABLE [dbo].[StockImport] (
    [id] INT IDENTITY(1,1) NOT NULL,
    [product_id] INT NOT NULL,
    [store_id] INT NOT NULL,
    [import_quantity] INT NOT NULL,
    [note] NVARCHAR(255) NULL,
    [created_at] DATETIME NOT NULL CONSTRAINT [DF_StockImport_created_at] DEFAULT (GETDATE()),
    [created_by] INT NULL,
    CONSTRAINT [PK_StockImport] PRIMARY KEY CLUSTERED ([id] ASC),
    CONSTRAINT [CK_StockImport_quantity] CHECK ([import_quantity] > 0),
    CONSTRAINT [FK_StockImport_Product] FOREIGN KEY ([product_id]) REFERENCES [dbo].[Product]([id]),
    CONSTRAINT [FK_StockImport_Store] FOREIGN KEY ([store_id]) REFERENCES [dbo].[Store]([store_id]),
    CONSTRAINT [FK_StockImport_Account] FOREIGN KEY ([created_by]) REFERENCES [dbo].[Account]([uID])
);
GO

CREATE TABLE [dbo].[Orders] (
    [id] INT IDENTITY(1,1) NOT NULL,
    [account_id] INT NULL,
    [totalPrice] INT NULL,
    [note] NVARCHAR(255) NULL,
    [create_date] DATE NULL CONSTRAINT [DF_Orders_create_date] DEFAULT (GETDATE()),
    [shipping_id] INT NULL,
    [store_id] INT NULL,
    [vat_percent] INT DEFAULT 10,
    CONSTRAINT [PK_Orders] PRIMARY KEY CLUSTERED ([id] ASC),
    CONSTRAINT [FK_Orders_Account] FOREIGN KEY ([account_id]) REFERENCES [dbo].[Account]([uID]),
    CONSTRAINT [FK_Orders_Shipping] FOREIGN KEY ([shipping_id]) REFERENCES [dbo].[Shipping]([id]),
    CONSTRAINT [FK_Orders_Store] FOREIGN KEY ([store_id]) REFERENCES [dbo].[Store]([store_id])
);
GO

CREATE TABLE [dbo].[OrderDetail] (
    [id] INT IDENTITY(1,1) NOT NULL,
    [order_id] INT NULL,
    [productName] NVARCHAR(255) NULL,
    [productImage] NVARCHAR(255) NULL,
    [productPrice] INT NULL,
    [quantity] INT NULL,
    CONSTRAINT [PK_OrderDetail] PRIMARY KEY CLUSTERED ([id] ASC),
    CONSTRAINT [FK_OrderDetail_Orders] FOREIGN KEY ([order_id]) REFERENCES [dbo].[Orders]([id])
);
GO

CREATE TABLE [dbo].[HomeSetting] (
    [id] INT NOT NULL,
    [hero_badge] NVARCHAR(255) NOT NULL,
    [hero_title] NVARCHAR(255) NOT NULL,
    [hero_highlight] NVARCHAR(255) NULL,
    [hero_description] NVARCHAR(1000) NOT NULL,
    [primary_button_text] NVARCHAR(100) NOT NULL,
    [secondary_button_text] NVARCHAR(100) NULL,
    [featured_title] NVARCHAR(255) NOT NULL,
    [show_stats] BIT NOT NULL DEFAULT 1,
    [show_filter_sidebar] BIT NOT NULL DEFAULT 1,
    [show_featured_section] BIT NOT NULL DEFAULT 1,
    [featured_mode] VARCHAR(20) NOT NULL DEFAULT 'newest',
    [featured_product_id] INT NULL,
    CONSTRAINT [PK_HomeSetting] PRIMARY KEY CLUSTERED ([id] ASC),
    CONSTRAINT [CK_HomeSetting_featured_mode] CHECK ([featured_mode] IN ('newest', 'price_desc', 'price_asc')),
    CONSTRAINT [FK_HomeSetting_Product] FOREIGN KEY ([featured_product_id]) REFERENCES [dbo].[Product]([id])
);
GO


CREATE TABLE [dbo].[Slider] (
    [id] INT IDENTITY(1,1) NOT NULL,
    [title] NVARCHAR(255) NULL,
    [image_url] NVARCHAR(MAX) NOT NULL,
    [back_link] NVARCHAR(MAX) NULL,
    [status] BIT NOT NULL DEFAULT 1,
    [description] NVARCHAR(1000) NULL,
    CONSTRAINT [PK_Slider] PRIMARY KEY CLUSTERED ([id] ASC)
);
GO

CREATE TABLE [dbo].[Voucher] (
    [id] INT IDENTITY(1,1) NOT NULL,
    [code] VARCHAR(50) NOT NULL,
    [discount_percent] INT NOT NULL,
    [max_discount] INT NULL,
    [min_order_value] INT NULL,
    [expiry_date] DATE NOT NULL, -- Đã đổi sang kiểu DATE để dễ nhập liệu
    [start_date] DATE NOT NULL DEFAULT GETDATE(),
    [store_id] INT NOT NULL,
    CONSTRAINT [PK_Voucher] PRIMARY KEY ([id]),
    CONSTRAINT [FK_Voucher_Store] FOREIGN KEY ([store_id]) REFERENCES [dbo].[Store]([store_id])
);
CREATE TABLE [dbo].[Feedback] (
    [id] INT IDENTITY(1,1) NOT NULL,
    [account_id] INT NOT NULL,
    [product_id] INT NOT NULL,
    [store_id] INT NOT NULL,
    [rating] INT NOT NULL,
    [content] NVARCHAR(1000) NULL,
    [create_date] DATETIME DEFAULT GETDATE(),
    [is_edited] BIT NOT NULL DEFAULT 0,
    [is_hidden] BIT NOT NULL DEFAULT 0,
    CONSTRAINT [PK_Feedback] PRIMARY KEY ([id]),
    CONSTRAINT [FK_Feedback_Account] FOREIGN KEY ([account_id]) REFERENCES [dbo].[Account]([uID]),
    CONSTRAINT [FK_Feedback_Product] FOREIGN KEY ([product_id]) REFERENCES [dbo].[Product]([id]),
    CONSTRAINT [FK_Feedback_Store] FOREIGN KEY ([store_id]) REFERENCES [dbo].[Store]([store_id])
);
GO


SET IDENTITY_INSERT [dbo].[Account] ON;
GO

INSERT INTO [dbo].[Account] ([uID], [user], [pass], [isAdmin], [role], [active], [fullname], [phone], [email], [address]) VALUES
(1, 'admin', 'pbkdf2$65536$ScS45maCrmAB5l2ghN8BiQ==$tlP+OMsjISUCIWtk7obU7QarX4dTqmywqb6CGFOG0aE=', 1, 'admin', 1, N'Quản trị hệ thống', N'0900000001', 'admin@prj301.local', N'Hà Nội'),
(2, 'owner_alpha', 'pbkdf2$65536$fwvajxgfE42GM8CXf/j8EQ==$0gkyIoHpS/kaVWVDr5uVzYIaau/UZeFVta3CNRUh2y0=', 0, 'owner', 1, N'Nguyễn Văn Alpha', N'0900000002', 'owner.alpha@prj301.local', N'Hồ Chí Minh'),
(3, 'owner_beta', 'pbkdf2$65536$n+UoK/nuTPbVY1n31KUdjA==$S9Ed6ZH0rPHUMj7qIhL75F8EOIYNMJS/btozDIXZNyk=', 0, 'owner', 1, N'Trần Thị Beta', N'0900000003', 'owner.beta@prj301.local', N'Đà Nẵng'),
(4, 'customer_01', 'pbkdf2$65536$JUu3s2/Ev1zUlIHhAmUgTw==$4XpLv7H+ggMNoluH+HEIp8HEQ1mx5ez2zpg3x2NHP/I=', 0, 'customer', 1, N'Lê Minh Khách 01', N'0900000004', 'customer01@prj301.local', N'Cần Thơ'),
(5, 'customer_02', 'pbkdf2$65536$9UF2E92mfXrf7t+vFQahbg==$yfPayyIQcO4Ka79GzNh6ZCNuIOpKkFVtM0Bwk96dpAk=', 0, 'customer', 1, N'Phạm Khách 02', N'0900000005', 'customer02@prj301.local', N'Hải Phòng'),
(6, 'customer_03', 'pbkdf2$65536$+Ls9R96fW8zFvzhNvVESTA==$mpAa3KLGQckIZqNV6fjBGDUAGzPJ/VqO4woMUxmMtto=', 0, 'customer', 1, N'Hoàng Khách 03', N'0900000006', 'customer03@prj301.local', N'Nghệ An'),
(7, 'customer_04', 'pbkdf2$65536$4/OLhcpRVWG8r6VxBhIL8w==$8rjp6G41vLvn3RZGM37SYpbyexZZNju3267iMY68hvI=', 0, 'customer', 1, N'Vũ Khách 04', N'0900000007', 'customer04@prj301.local', N'Bình Dương'),
(8, 'customer_05', 'pbkdf2$65536$v6O6hGG6ND+JoxwK/E6+bg==$zfVs1+gx8xDSKW/Qs9SE7JzVO0fIRw/ywU7PrDYdn8I=', 0, 'customer', 0, N'Đỗ Khách 05', N'0900000008', 'customer05@prj301.local', N'Huế'),
(9, 'shipper_alpha', 'pbkdf2$65536$v6O6hGG6ND+JoxwK/E6+bg==$zfVs1+gx8xDSKW/Qs9SE7JzVO0fIRw/ywU7PrDYdn8I=', 0, 'shipper', 1, N'Nguyễn Văn Shipper Alpha', N'0900000009', 'shipper.alpha@prj301.local', N'HCM'),
(10, 'shipper_beta', 'pbkdf2$65536$v6O6hGG6ND+JoxwK/E6+bg==$zfVs1+gx8xDSKW/Qs9SE7JzVO0fIRw/ywU7PrDYdn8I=', 0, 'shipper', 1, N'Trần Văn Shipper Beta', N'0900000010', 'shipper.beta@prj301.local', N'Đà Nẵng'),
(11, 'warehouse_alpha', 'pbkdf2$65536$ScS45maCrmAB5l2ghN8BiQ==$tlP+OMsjISUCIWtk7obU7QarX4dTqmywqb6CGFOG0aE=', 0, 'warehouse_manager', 1, N'Quản lý kho Alpha', N'0900000011', 'warehouse.alpha@prj301.local', N'Hồ Chí Minh'),
(12, 'warehouse_beta', 'pbkdf2$65536$ScS45maCrmAB5l2ghN8BiQ==$tlP+OMsjISUCIWtk7obU7QarX4dTqmywqb6CGFOG0aE=', 0, 'warehouse_manager', 1, N'Quản lý kho Beta', N'0900000012', 'warehouse.beta@prj301.local', N'Đà Nẵng');

GO

SET IDENTITY_INSERT [dbo].[Account] OFF;
GO

INSERT INTO [dbo].[HomeSetting] (
    [id], [hero_badge], [hero_title], [hero_highlight], [hero_description],
    [primary_button_text], [secondary_button_text], [featured_title],
    [show_stats], [show_filter_sidebar], [show_featured_section], [featured_mode], [featured_product_id]
) VALUES (
    1,
    N'Bộ sưu tập nổi bật',
    N'Nâng cấp phong cách mỗi ngày',
    N'Sneaker',
    N'Khám phá bộ sưu tập giày sneaker đa dạng và thời thượng của chúng tôi, nơi phong cách gặp gỡ sự thoải mái. Từ những thiết kế cổ điển đến những mẫu mã mới nhất, chúng tôi có tất cả để bạn lựa chọn. Nâng cấp phong cách của bạn mỗi ngày với những đôi giày sneaker chất lượng cao, phù hợp với mọi dịp và cá tính.',
    N'Xem thêm',
    N'Mua ngay',
    N'Sản phẩm nổi bật',
    1,
    1,
    1,
    'newest',
    NULL
);
GO

INSERT INTO [dbo].[Slider] (title, image_url, back_link, status, description) VALUES
(N'Adidas Stan Smith Special', N'https://th.bing.com/th/id/OIP.YdN1gfY_9e6RpjFnMm7a1gHaHa?w=201&h=201&c=7&r=0&o=7&dpr=1.5&pid=1.7&rm=3', N'detail?productId=1', 1, N'Mẫu giày huyền thoại với phong cách tối giản.'),
(N'Vans Old Skool Collection', N'https://th.bing.com/th/id/OIP.B56S_fHRq-6NHGKS4I1WmQHaGC?w=247&h=201&c=7&r=0&o=7&dpr=1.5&pid=1.7&rm=3', N'detail?productId=2', 1, N'Sự lựa chọn hoàn hảo cho phong cách đường phố.'),
(N'Khám Phá Converse Chuck 70', N'https://th.bing.com/th/id/OIP.ZIVkD33qBh_HLeE8i6PTjAHaLH?w=134&h=201&c=7&r=0&o=7&dpr=1.5&pid=1.7&rm=3', N'detail?productId=3', 1, N'Thiết kế cổ điển kết hợp phong cách hiện đại.');
GO

INSERT INTO [dbo].[Store] ([store_name], [owner_id], [shipper_id], [warehouse_manager_id]) VALUES
(N'Alpha Sneaker Store', 2, 9, 11),
(N'Beta Shoe House', 3, 10, 12);
GO
-- =============================================
-- 1. Insert Categories (Danh mục sản phẩm)
-- =============================================
SET IDENTITY_INSERT [dbo].[Category] ON;
GO
INSERT INTO [dbo].[Category] ([cid], [cname], [store_id]) VALUES
(1, N'Giày Nike', 1),
(2, N'Giày Adidas', 1),
(3, N'Giày Vans', 2),
(4, N'Giày Converse', 2);
GO
SET IDENTITY_INSERT [dbo].[Category] OFF;
GO

-- =============================================
-- 2. Insert Products (Sản phẩm)
-- =============================================
SET IDENTITY_INSERT [dbo].[Product] ON;
GO
-- store_id = 1 (Alpha Store - owner_id: 2)
-- store_id = 2 (Beta Store - owner_id: 3)
INSERT INTO [dbo].[Product] ([id], [name], [image], [price], [title], [description], [cateID], [quantity], [sell_ID], [store_id]) VALUES
(1, N'Adidas Stan Smith', N'https://th.bing.com/th/id/OIP.YdN1gfY_9e6RpjFnMm7a1gHaHa?w=201&h=201&c=7&r=0&o=7&dpr=1.5&pid=1.7&rm=3', 2100000, 45, N'Giày sneaker cổ điển Stan Smith thiết kế tối giản.', 2, 60, 2, 1),
(2, N'Vans Old Skool', N'https://th.bing.com/th/id/OIP.B56S_fHRq-6NHGKS4I1WmQHaGC?w=247&h=201&c=7&r=0&o=7&dpr=1.5&pid=1.7&rm=3', 1800000, 44, N'Giày trượt ván Vans Old Skool đen trắng huyền thoại.', 3, 100, 3, 2),
(3, N'Converse Chuck 70', N'https://th.bing.com/th/id/OIP.ZIVkD33qBh_HLeE8i6PTjAHaLH?w=134&h=201&c=7&r=0&o=7&dpr=1.5&pid=1.7&rm=3', 2000000, 35, N'Giày Converse cổ cao Chuck 70 Vintage chất liệu canvas cao cấp.', 4, 80, 3, 2),
(4, N'Adidas Stan Smith', N'https://th.bing.com/th/id/OIP.TEjla2lz1uDJkQCJFbCCLgHaHa?w=180&h=180&c=7&r=0&o=7&dpr=1.5&pid=1.7&rm=3', 2100000, 45, N'Giày sneaker cổ điển Stan Smith thiết kế tối giản.', 2, 60, 2, 1),
(5, N'Vans Old Skool', N'https://th.bing.com/th/id/OIP.oW2GArOVmCsVD6suv5bEwAHaHa?w=219&h=219&c=7&r=0&o=7&dpr=1.5&pid=1.7&rm=3', 1800000, 44, N'Giày trượt ván Vans Old Skool đen trắng huyền thoại.', 3, 100, 3, 2),
(6, N'Converse Chuck 70', N'https://th.bing.com/th/id/OIP.RISGC4rnftddlIoEQnWjfQHaHa?w=219&h=219&c=7&r=0&o=7&dpr=1.5&pid=1.7&rm=3', 2000000, 35, N'Giày Converse cổ cao Chuck 70 Vintage chất liệu canvas cao cấp.', 4, 80, 3, 2);

GO
SET IDENTITY_INSERT [dbo].[Product] OFF;
GO

-- =============================================
-- 3. Insert StockImport (Lịch sử nhập kho)
-- =============================================
SET IDENTITY_INSERT [dbo].[StockImport] ON;
GO
INSERT INTO [dbo].[StockImport] ([id], [product_id], [store_id], [import_quantity], [note], [created_at], [created_by]) VALUES
(1, 1, 1, 50, N'Nhập hàng đợt 1', GETDATE(), 2),
(2, 2, 1, 30, N'Size 39: 10 đôi, Size 40: 20 đôi', GETDATE(), 11),
(3, 3, 1, 40, N'Size 41: 18 đôi, Size 42: 22 đôi', GETDATE(), 11),
(4, 4, 1, 60, N'Size 42: 30 đôi, Size 43: 30 đôi', GETDATE(), 11),
(5, 5, 2, 100, N'Size 39: 50 đôi, Size 40: 50 đôi', GETDATE(), 12),
(6, 6, 2, 80, N'Size 41: 40 đôi, Size 42: 40 đôi', GETDATE(), 12);
GO
SET IDENTITY_INSERT [dbo].[StockImport] OFF;
GO

-- =============================================
-- 4. Insert Cart (Giỏ hàng)
-- =============================================
-- AccountID 4, 5 là customer
INSERT INTO [dbo].[Cart] ([AccountID], [ProductID], [Amount], [reserved_at], [expires_at]) VALUES
(4, 1, 1, GETDATE(), DATEADD(day, 1, GETDATE())),
(4, 3, 2, GETDATE(), DATEADD(day, 1, GETDATE())),
(5, 5, 1, GETDATE(), DATEADD(day, 1, GETDATE()));
GO

-- =============================================
-- 5. Insert Shipping (Giao hàng)
-- =============================================
SET IDENTITY_INSERT [dbo].[Shipping] ON;
GO
-- shipper_id = 9 cho store 1, shipper_id = 10 cho store 2
INSERT INTO [dbo].[Shipping] ([id], [name], [phone], [address], [Status], [shipper_id], [store_id], [shipped_date]) VALUES
(1, N'Giao cho khách Hoàng Khách 03', N'0900000006', N'Nghệ An', 'Shipped', 9, 1, GETDATE()),
(2, N'Giao cho khách Vũ Khách 04', N'0900000007', N'Bình Dương', 'In Transit', 10, 2, NULL),
(3, N'Giao cho khách Lê Minh Khách 01', N'0900000004', N'Cần Thơ', 'Pending', 9, 1, NULL);
GO
SET IDENTITY_INSERT [dbo].[Shipping] OFF;
GO

-- =============================================
-- 6. Insert Orders (Đơn hàng)
-- =============================================
SET IDENTITY_INSERT [dbo].[Orders] ON;
GO
INSERT INTO [dbo].[Orders] ([id], [account_id], [totalPrice], [note], [create_date], [shipping_id], [store_id]) VALUES
(1, 6, 2500000, N'Giao trong giờ hành chính', GETDATE(), 1, 1),
(2, 7, 3800000, N'Gói quà tặng', GETDATE(), 2, 2),
(3, 4, 2500000, N'Gọi trước khi giao', GETDATE(), 3, 1);
GO
SET IDENTITY_INSERT [dbo].[Orders] OFF;
GO

-- =============================================
-- 7. Insert OrderDetail (Chi tiết đơn hàng)
-- =============================================
SET IDENTITY_INSERT [dbo].[OrderDetail] ON;

INSERT INTO [dbo].[OrderDetail]
([id], [order_id], [productName], [productImage], [productPrice], [quantity])
VALUES
-- Order 1
(1, 1, N'Adidas Stan Smith',
 N'https://th.bing.com/th/id/OIP.TEjla2lz1uDJkQCJFbCCLgHaHa?w=180&h=180&c=7&r=0&o=7&dpr=1.5&pid=1.7&rm=3',
 2100000, 1),

-- Order 2
(2, 2, N'Vans Old Skool',
 N'https://th.bing.com/th/id/OIP.oW2GArOVmCsVD6suv5bEwAHaHa?w=219&h=219&c=7&r=0&o=7&dpr=1.5&pid=1.7&rm=3',
 1800000, 1),

(3, 2, N'Converse Chuck 70',
 N'https://th.bing.com/th/id/OIP.RISGC4rnftddlIoEQnWjfQHaHa?w=219&h=219&c=7&r=0&o=7&dpr=1.5&pid=1.7&rm=3',
 2000000, 1),

-- Order 3
(4, 3, N'Adidas Stan Smith',
 N'https://th.bing.com/th/id/OIP.TEjla2lz1uDJkQCJFbCCLgHaHa?w=180&h=180&c=7&r=0&o=7&dpr=1.5&pid=1.7&rm=3',
 2100000, 1);
 go
 INSERT INTO [dbo].[Feedback] ([account_id], [product_id], [store_id], [rating], [content]) VALUES
(4, 1, 1, 5, N'Giày đi rất êm và đẹp, đúng size.'),
(5, 2, 2, 4, N'Chất lượng tốt, đóng gói kỹ.'),
(6, 3, 2, 5, N'Hàng chính hãng, rất hài lòng.');
GO
SET IDENTITY_INSERT [dbo].[OrderDetail] OFF;
GO
-- =============================================
-- 8. Insert News (Tin tức)
-- =============================================
CREATE TABLE [dbo].[News] (
    [id] INT IDENTITY(1,1) NOT NULL,
    [title] NVARCHAR(255) NOT NULL,
    [content] NVARCHAR(MAX) NOT NULL,
    [image] NVARCHAR(MAX) NULL,
    [created_at] DATETIME DEFAULT GETDATE(),
    [store_id] INT NULL, -- NULL = Tin tức hệ thống (Admin), NOT NULL = Tin tức của Store
    [is_visible] BIT NOT NULL DEFAULT 1,
    CONSTRAINT [PK_News] PRIMARY KEY ([id]),
    CONSTRAINT [FK_News_Store] FOREIGN KEY ([store_id]) REFERENCES [dbo].[Store]([store_id]) ON DELETE CASCADE
);
GO

INSERT INTO [dbo].[News] ([title], [content], [image], [store_id]) VALUES
(N'Chào mừng V-SNKR ra mắt!', N'Hệ thống sàn thương mại điện tử chuyên Sneaker chính thức đi vào hoạt động.', 'https://images.unsplash.com/photo-1552346154-21d32810aba3?q=80&w=2070&auto=format&fit=crop', NULL),
(N'Khuyến mãi khai trương Shop Alpha', N'Giảm giá toàn bộ sản phẩm tại Alpha Sneakers trong tuần lễ đầu tiên.', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?q=80&w=2070&auto=format&fit=crop', 1);
GO

-- =============================================
-- 9. Insert Contact (Liên hệ hỗ trợ đơn hàng)
-- =============================================
CREATE TABLE [dbo].[Contact] (
    [id] INT IDENTITY(1,1) NOT NULL,
    [account_id] INT NOT NULL,
    [order_id] INT NOT NULL,
    [store_id] INT NOT NULL,
    [message] NVARCHAR(MAX) NOT NULL,
    [response_message] NVARCHAR(MAX) NULL,
    [responded_at] DATETIME NULL,
    [created_at] DATETIME DEFAULT GETDATE(),
    [status] NVARCHAR(50) DEFAULT N'Chờ xử lý', -- Chờ xử lý, Đã phản hồi
    CONSTRAINT [PK_Contact] PRIMARY KEY ([id]),
    CONSTRAINT [FK_Contact_Account] FOREIGN KEY ([account_id]) REFERENCES [dbo].[Account]([uID]),
    CONSTRAINT [FK_Contact_Orders] FOREIGN KEY ([order_id]) REFERENCES [dbo].[Orders]([id]),
    CONSTRAINT [FK_Contact_Store] FOREIGN KEY ([store_id]) REFERENCES [dbo].[Store]([store_id])
);
GO

INSERT INTO [dbo].[Contact] ([account_id], [order_id], [store_id], [message]) VALUES
(4, 3, 1, N'Tôi nhận hàng rồi nhưng size hơi chật, shop hỗ trợ đổi trả được không?');
GO
-- =============================================
-- 10. Staff Action History (Lịch sử quản lý nhân viên)
-- =============================================
CREATE TABLE [dbo].[StaffActionHistory] (
    [id] INT IDENTITY(1,1) NOT NULL,
    [owner_id] INT NOT NULL,          -- Người thực hiện (Owner)
    [staff_id] INT NOT NULL,          -- Nhân viên bị tác động
    [action_type] NVARCHAR(50) NOT NULL, -- 'ADD' or 'UPDATE'
    [details] NVARCHAR(MAX) NULL,     -- Chi tiết thay đổi
    [action_at] DATETIME DEFAULT GETDATE(),
    CONSTRAINT [PK_StaffActionHistory] PRIMARY KEY ([id]),
    CONSTRAINT [FK_History_Owner] FOREIGN KEY ([owner_id]) REFERENCES [dbo].[Account]([uID]),
    CONSTRAINT [FK_History_Staff] FOREIGN KEY ([staff_id]) REFERENCES [dbo].[Account]([uID])
);
GO
