package businessLogic.validators;

public interface Validator<T> {
    public String validate(T t);
}
