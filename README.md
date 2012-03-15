# Groovy Slf4j Nested Diagnostic Context (NDC)

This library implements a nested diagnostic context (NDC) for slf4j logging facade and provides Groovy AST support
for populating it.  It does this by leveraging slf4j's existing support for mapped diagnostic context (MDC) and
adding push/pop functionality to MDC keys.

## Using NDC directly

    NDC.push("key", "new message")
    try {
        // ... do something ...
    } finally {
        NDC.pop("key")
    }

## Using @NDC annotation on Groovy methods

### Simple usage

This will cause "Foo.bar" to be pushed onto the "method" MDC when the method is called, and popped when it completes,
either successfully or by thrown exception:

    class Foo {
        @NDC(key="method")
        void bar() {
            // ...
        }
    }

This will do much the same, but will push "baz" instead:

    class Foo {
        @NDC(key="method", baseMessage="baz")
        void bar() {
            // ...
        }
    }

You can also add parameters to the message based on variables, fields, or bean properties.  This will cause
"bar index=1 name=Frank" to be pushed if invoked with an index of 1 and with a Person bean with a name of "Frank":

    class Foo {
        @NDC(key="method", baseMessage="bar", params=[
            @NDCParam('index'), @NDCParam(name='name', value='person.name')
        ])
        void bar(int index, Person person) {
            // ...
        }
    }

