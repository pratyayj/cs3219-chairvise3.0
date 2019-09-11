const isProduction = process.env.NODE_ENV === 'production';

module.exports = {
  publicPath: isProduction ? '/web/' : '/',
  outputDir: '../../main/webapp/dist',
  assetsDir: isProduction ? '../assets' : '.',
  devServer: {
    proxy: 'http://localhost:8080'
  }
};