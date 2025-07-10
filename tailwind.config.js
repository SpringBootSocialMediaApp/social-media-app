/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './src/main/resources/templates/**/*.html',
    './src/main/resources/static/js/**/*.js'
  ],
  theme: {
    extend: {
      colors: {
        brand: {
          50 : '#c4c4c4',
          100: '#ebf9fe',
          200: '#65cbf8',
          300: '#10bbff',
          400: '#1078ff',
          500: '#004296',
        },


        navy: {
            50: '#FBFDFE',
            100: '#F7FBFD',
            200: '#EBF4FA',
            300: '#D7E7F4',
            400: '#BDD3EC',
            500: '#9DB7E0',
            600: '#7991D2',
            700: '#5265BF',
            800: '#3B4893',
            900: '#2A305F',
            950: '#15172C',
          },

      }

    },
  },
  plugins: [
    require('daisyui'),
  ],
  daisyui: {
    themes: ['socialApp', 'dark', 'cupcake'],
  },
}
