package br.com.mvc.model.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.mvc.model.client.FaturaResponse;
import br.com.mvc.model.client.FaturamentoClient;
import br.com.mvc.model.entity.Product;
import br.com.mvc.model.entity.Sale;
import br.com.mvc.model.entity.SaleStatus;
import br.com.mvc.model.repository.ProductRepository;
import br.com.mvc.model.repository.SaleRepository;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final FaturamentoClient faturamentoClient;

    public SaleService(SaleRepository saleRepository,
                       ProductRepository productRepository,
                       FaturamentoClient faturamentoClient) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.faturamentoClient = faturamentoClient;
    }

    public List<Sale> read() {
        return saleRepository.findAll();
    }

    /** Abre um pedido (ainda não faturado). */
    @Transactional
    public Sale create(BigInteger productId, BigInteger quantity) {
        if (quantity == null || quantity.signum() <= 0) {
            throw new RuntimeException("A quantidade deve ser maior que zero");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (product.getPrice() == null) {
            throw new RuntimeException("Produto sem preço cadastrado");
        }

        Sale sale = Sale.builder()
                .product(product)
                .quantity(quantity)
                .total(product.getPrice().multiply(new BigDecimal(quantity)))
                .status(SaleStatus.ABERTO)
                .build();

        return saleRepository.save(sale);
    }

    /**
     * Fecha o pedido: envia ao hub de faturamento (REST) e guarda o ID da
     * fatura devolvido. Se o hub falhar, a exceção sobe e o pedido
     * continua ABERTO.
     */
    @Transactional
    public Sale close(BigInteger saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (sale.getStatus() == SaleStatus.FECHADO) {
            throw new RuntimeException("O pedido já está fechado");
        }

        FaturaResponse fatura = faturamentoClient.faturar(sale.getId().longValue(), sale.getTotal());

        sale.setFaturaId(fatura.faturaId());
        sale.setStatus(SaleStatus.FECHADO);
        return saleRepository.save(sale);
    }
}