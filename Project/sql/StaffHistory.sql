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
