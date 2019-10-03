package com.foodaholic.items;

public class ItemPromo {

    private String code, value, type, minimum_order;

    public ItemPromo(String code, String value, String type, String minimum_order)
    {
        this.code = code;
        this.value = value;
        this.type = type;
        this.minimum_order = minimum_order;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public double getMinimum_order() {
        return Double.valueOf(minimum_order);
    }

    public String getDiscount(String total)
    {
        if(type.equals("Flat"))
        {
            return value;
        }
        else
        {
            double d = Double.parseDouble(value)*Double.parseDouble(total);

            double c = d/100.0;

            return String.format("%.2f", c);
        }
    }


    public String amountAfterDiscount(String total)
    {
        double discount = Double.parseDouble(getDiscount(total));
        double t = Double.parseDouble(total);

        return String.format("%.2f", t-discount);

    }
}
