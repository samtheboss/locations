/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;


/**
 *
 * @author samue
 */

public class WhereBy {
    public String column;
    public Object value;
    public Equate equate;

    public WhereBy(String column, Object value, Equate equate) {
        this.column = column;
        this.value = value;
        this.equate = equate;
    }
    public WhereBy(String column, Object value) {
        this(column, value, Equate.EQUALS);
    }
    public  enum Equate {
        EQUALS,
        GREATER_THAN,
        LESS_THAN,
        LIKE,
        BETWEEN
    }
}
