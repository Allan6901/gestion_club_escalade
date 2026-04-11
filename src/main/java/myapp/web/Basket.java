package myapp.web;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;

@Data
public class Basket {

    private List<String> products = new LinkedList<>();

}