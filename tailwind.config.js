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
          100: '#ebf9fe',
          200: '#c7effd',
          300: '#a3e5fc',
          400: '#11bdf8',
          500: '#0fa8e0',
        }
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
