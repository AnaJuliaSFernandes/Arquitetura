package br.com.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.mvc.model.entity.Product;
import br.com.mvc.model.service.ProductService;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    public String read(Model model){
        model.addAttribute("products", productService.read());
        model.addAttribute("product", Product.builder().build());
        return "products";

    }

    @PostMapping
    public String create(@ModelAttribute Product product){
        productService.create(product);
        return "redirect:/product";
    }

    @PostMapping("/{id}/descontinuar")
    public String discontinue(@PathVariable java.math.BigInteger id,
                               @RequestParam String perfil) {
        productService.discontinue(id, perfil);
        return "redirect:/product";
    }

}
