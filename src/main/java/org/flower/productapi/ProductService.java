package org.flower.productapi;

@Component
public class ProductService {

    @Resource
    ProductRepository productRepository;

    Long saveProduct(Product product) {
        ProductDAO saved = productRepository.saveAndFlush(toDAO(product));

        return saved.getProductId();
    }

    Product getProductById(Long productId) throws ProductNotFoundException {
        return this.productRepository.findById(productId).map(dao -> {
            Product found = new Product();
            found.setProductId(dao.getProductId());
            found.setName(dao.getName());
            found.setSku(dao.getSku());
            found.setDescription(dao.getDescription());
            return found;
        }).orElseThrow(new ProductNotFoundException("Product not found"));
    }

    public int getTotalProductCount() {
        return this.productRepository.count();
    }

    public List<Product> getProducts(int count, int page) {
        Page<ProductDAO> results = this.productRepository.findAll(Pageable.ofSize(count).withPage(page));
        List<Product> toReturn = results.map(this::toDTO).toList();
        return toReturn;
    }

    private ProductDAO toDAO(Product product) {
        ProductDAO dao = new ProductDAO();
        dao.setName(product.getName());
        dao.setDescription(product.getDescription());
        dao.setSku(product.getSku());
        return dao;
    }
    private Product toDTO(ProductDAO dao) {
        Product product = new Product();
        product.setProductId(dao.getProductId());
        product.setDescription(dao.getDescription());
        product.setSku(dao.getSku());
        product.setName(dao.getName());
        return product;
    }

}