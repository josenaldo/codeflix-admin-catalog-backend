package br.com.josenaldo.codeflix.catalog.infrastructure.testutils;

import com.github.f4b6a3.ulid.Ulid;
import java.util.function.Supplier;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.description.Description;

public class ValidUlidAssert extends AbstractAssert<ValidUlidAssert, String> {

    public ValidUlidAssert(String actualId) {
        super(actualId, ValidUlidAssert.class);
    }

    public static ValidUlidAssert assertThat(String actualId) {
        return new ValidUlidAssert(actualId);
    }

    public ValidUlidAssert isValidUlid(String actualId) {
        isNotNull();

        if (!Ulid.isValid(actualId)) {
            failWithMessage("%s is not a valid ULID".formatted(actualId));
        }

        return this;
    }


    @Override
    public ValidUlidAssert as(Description description) {
        return super.as(description);
    }


    @Override
    public ValidUlidAssert describedAs(String description, Object... args) {
        return super.describedAs(description, args);
    }


    @Override
    public ValidUlidAssert describedAs(Supplier<String> descriptionSupplier) {
        return super.describedAs(descriptionSupplier);
    }
}
