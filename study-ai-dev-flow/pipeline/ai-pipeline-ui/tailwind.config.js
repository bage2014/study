/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        base: '#FAFAFA',
        surface: '#FFFFFF',
        ink: {
          DEFAULT: '#0A0A0A',
          secondary: '#525252',
          muted: '#A3A3A3',
        },
        line: {
          DEFAULT: '#E5E5E5',
          strong: '#D4D4D4',
        },
        accent: {
          DEFAULT: '#2563EB',
          hover: '#1D4ED8',
          soft: '#EFF4FF',
        },
        ok: {
          DEFAULT: '#16A34A',
          soft: '#F0FDF4',
        },
        warn: {
          DEFAULT: '#D97706',
          soft: '#FFFBEB',
        },
        danger: {
          DEFAULT: '#DC2626',
          soft: '#FEF2F2',
        },
      },
      fontFamily: {
        sans: [
          '-apple-system', 'BlinkMacSystemFont', 'Inter',
          'PingFang SC', 'Helvetica Neue', 'Segoe UI', 'sans-serif',
        ],
        mono: [
          'SF Mono', 'JetBrains Mono', 'Menlo', 'Consolas', 'monospace',
        ],
      },
      fontSize: {
        '2xs': ['11px', '14px'],
      },
      borderRadius: {
        sm: '4px',
        DEFAULT: '6px',
        md: '6px',
        lg: '8px',
      },
      boxShadow: {
        none: 'none',
        focus: '0 0 0 3px rgba(37, 99, 235, 0.12)',
      },
    },
  },
  plugins: [],
}
