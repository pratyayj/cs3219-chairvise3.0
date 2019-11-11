const isProduction = process.env.NODE_ENV === 'production';

module.exports = {
  publicPath: isProduction ? '/web/' : '/',
  outputDir: '../../main/webapp/dist',
  assetsDir: isProduction ? '../assets' : '.',
  devServer: {
    proxy: 'http://localhost:8080'
  },
  configureWebpack: {
    externals: {
      // Chart.js imports moment by default. We can exclude moment from the
      // bundle since we don't use Chart.js time functionalities.
      moment: 'moment'
    }
  }
};