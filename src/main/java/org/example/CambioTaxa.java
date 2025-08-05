package org.example;
public class CambioTaxa {
    private String result;
    private String base_code;
    private String target_code;
    private double conversion_rate;
    private String error_type;


    public String getResult() {
        return result;
    }

    public String getBaseCode() {
        return base_code;
    }

    public String getTargetCode() {
        return target_code;
    }

    public double getConversionRate() {
        return conversion_rate;
    }

    public String getErrorType() {
        return error_type;
    }


    public void setResult(String result) {
        this.result = result;
    }

    public void setBaseCode(String base_code) {
        this.base_code = base_code;
    }

    public void setTargetCode(String target_code) {
        this.target_code = target_code;
    }

    public void setConversionRate(double conversion_rate) {
        this.conversion_rate = conversion_rate;
    }

    public void setErrorType(String error_type) {
        this.error_type = error_type;
    }

    @Override
    public String toString() {
        return String.format("CambioTaxa{result='%s', base='%s', target='%s', rate=%.4f}",
                result, base_code, target_code, conversion_rate);
    }
}
