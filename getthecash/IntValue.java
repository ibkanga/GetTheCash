package getthecash;
// @author IgorBarašin2681

public class IntValue {
    
    private int value;
    
    public IntValue() {
        
    }
    
    public IntValue(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public void setValue(int value) {
        this.value = value;
    }
    
    public void incrementValue() {
        value++;
    }
    
    public void decrementValue() {
        value--;
    }
    
}

