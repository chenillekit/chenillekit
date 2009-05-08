registry = bsf.lookupBean('registry');

x = "Hello World from 'test2.groovy' script";
y = x;

print(registry.getClass().getName());

return y;