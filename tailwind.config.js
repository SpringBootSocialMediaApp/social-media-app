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
            50 : '#c4c4c4',
            100: '#ebf9fe',
            200: '#65cbf8',
            300: '#10bbff',
            400: '#1078ff',
            500: '#004296',
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
