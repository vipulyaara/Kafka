module.exports = {
  env: {
    es6: true,
    node: true,
  },
  parserOptions: {
    "ecmaVersion": 2018,
  },
  extends: [
    "eslint:recommended",
    "google",
  ],
  rules: {
    "no-restricted-globals": ["error", "name", "length"],
    "prefer-arrow-callback": "error",
    "quotes": ["error", "double", {"allowTemplateLiterals": true}],
    "no-trailing-spaces": "off",
    "eol-last": "off",
    "indent": "off",
    "max-len": "off",
    "quotes": "off",
    "comma-dangle": "off",
    "object-curly-spacing": "off",
    "semi": "off",
    "padded-blocks": "off",
    "no-multiple-empty-lines": "off",
    "require-jsdoc": "off",
    "arrow-parens": "off",
    "guard-for-in": "off",
    "no-unused-vars": "off",
    "no-dupe-keys": "off",
  },
  overrides: [
    {
      files: ["**.spec.*"],
      env: {
        mocha: true,
      },
      rules: {},
    },
  ],
  globals: {},
};



