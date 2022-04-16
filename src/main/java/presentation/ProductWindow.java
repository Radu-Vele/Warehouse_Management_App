package presentation;

import javax.swing.*;

public class ProductWindow extends JFrame{
    private JPanel mainPanel;
    private JTabbedPane tabPanel;
    private JPanel addTab;
    private JPanel editTab;
    private JPanel deleteTab;
    private JPanel viewAllTab;
    private JPanel inputPanel;
    private JPanel buttonPanel;
    private JTextField titleField;
    private JTextField manufacturerField;
    private JTextField itemsInStockField;
    private JButton insertProductButton;
    private JLabel titleLabel;
    private JLabel successAddLabel;
    private JPanel inputIDPanel;
    private JPanel changedDataPanel;
    private JTextField searchIDField;
    private JTextField titleEditField;
    private JTextField manufacturerEditField;
    private JTextField itemsInStockEditField;
    private JCheckBox editTitleCheckBox;
    private JCheckBox editManufacturerCheckBox;
    private JCheckBox editNumberOfItemsCheckBox;
    private JButton editProductButton;
    private JLabel successEditLabel;
    private JTextField deleteIDField;
    private JButton deleteProductButton;
    private JLabel deleteIDLabel;
    private JLabel successDeleteLabel;
    private JButton viewAllProductsButton;
    private JTable productTable;

    ProductController productController;

    public ProductWindow() {
        setTitle("Products Management Form");
        setSize(500, 600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);
        productController = new ProductController(this);
        successAddLabel.setVisible(false);
        successEditLabel.setVisible(false);
        successDeleteLabel.setVisible(false);

        insertProductButton.addActionListener(productController);
        editProductButton.addActionListener(productController);
        deleteProductButton.addActionListener(productController);
        viewAllProductsButton.addActionListener(productController);
    }

    public JButton getInsertProductButton() {
        return insertProductButton;
    }

    public JTextField getTitleField() {
        return titleField;
    }

    public JTextField getManufacturerField() {
        return manufacturerField;
    }

    public JTextField getItemsInStockField() {
        return itemsInStockField;
    }

    public JLabel getSuccessAddLabel() {
        return successAddLabel;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JTextField getSearchIDField() {
        return searchIDField;
    }

    public JTextField getTitleEditField() {
        return titleEditField;
    }

    public JTextField getManufacturerEditField() {
        return manufacturerEditField;
    }

    public JTextField getItemsInStockEditField() {
        return itemsInStockEditField;
    }

    public JCheckBox getEditTitleCheckBox() {
        return editTitleCheckBox;
    }

    public JCheckBox getEditManufacturerCheckBox() {
        return editManufacturerCheckBox;
    }

    public JCheckBox getEditNumberOfItemsCheckBox() {
        return editNumberOfItemsCheckBox;
    }

    public JLabel getSuccessEditLabel() {
        return successEditLabel;
    }

    public JButton getEditProductButton() {
        return editProductButton;
    }

    public JButton getDeleteProductButton() {
        return deleteProductButton;
    }

    public JLabel getSuccessDeleteLabel() {
        return successDeleteLabel;
    }

    public JTextField getDeleteIDField() {
        return deleteIDField;
    }

    public JButton getViewAllProductsButton() {
        return viewAllProductsButton;
    }

    public JTable getProductTable() {
        return productTable;
    }
}
