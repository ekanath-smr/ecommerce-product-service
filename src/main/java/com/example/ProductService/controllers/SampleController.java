package com.example.ProductService.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// This annotation indicates that this class is a Spring MVC Controller
// RestController -> host Rest HTTP APIs.
@RestController
@RequestMapping("/sample") // "localhost:8080/sample"
public class SampleController {
    // This is a sample controller class.
    // You can add methods here to handle HTTP requests.

    @GetMapping("/hello/{name}/{times}") // "localhost:8080/sample/hello"
    public String sayHello(@PathVariable("name") String name, @PathVariable("times") int times) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < times; i++) {
            // new line will not work on the front end HTML page, you need to use line brake
            // stringBuilder.append("Hello, ").append(name).append("\n");
            stringBuilder.append("Hello, ").append(name).append("<br>");

        }
        return stringBuilder.toString();
    }

    @GetMapping("/hi") // "localhost:8080/sample/hi"
    public String sayHi() {
        return "Hi, this is a hi sample method";
    }
}


/*
amazon.in/orders => OrderController.java
/orders ==> OrderController.java ==> @RequestMapping("/orders")
"/orders" ==> this is called as endpoint

amazon.in/products ==> ProductController.java
amazon.in/users ==> UserController.java
amazon.in/payments ==> PaymentController.java

But here "amazon.in" is the domain name, we don't have the domain name
Our project is going to run on the local machine.
Default domain name your local machine is "localhost"
"8080" is the default port number.
On one port number, only one application can run.
*/