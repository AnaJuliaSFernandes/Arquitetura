package br.com.mvc.controller;

import java.math.BigInteger;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.mvc.model.service.ProductService;
import br.com.mvc.model.service.SaleService;

@Controller
@RequestMapping("/sales")
public class SaleController {

    private final SaleService saleService;
    private final ProductService productService;

    public SaleController(SaleService saleService, ProductService productService) {
        this.saleService = saleService;
        this.productService = productService;
    }

    @GetMapping
    public String read(Model model) {
        model.addAttribute("sales", saleService.read());
        model.addAttribute("products", productService.read());
        return "sales";
    }

    @PostMapping
    public String create(@RequestParam BigInteger productId, @RequestParam BigInteger quantity) {
        saleService.create(productId, quantity);
        return "redirect:/sales";
    }

    @PostMapping("/{id}/close")
    public String close(@PathVariable BigInteger id) {
        saleService.close(id);
        return "redirect:/sales";
    }
}