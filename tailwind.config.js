/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,jsx}'],
  theme: {
    extend: {
      colors: {
        navy: {
          950: '#17212b',
          900: '#1d2a36',
          800: '#243341',
        },
        brand: {
          50: '#eafaf6',
          100: '#cdf2e7',
          500: '#129488',
          600: '#0e766d',
          700: '#0b5f58',
        },
        page: '#f1f2f7',
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', 'sans-serif'],
      },
    },
  },
  plugins: [],
}
