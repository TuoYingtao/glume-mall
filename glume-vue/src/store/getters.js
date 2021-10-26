const getters = {
  sidebar: state => state.app.sidebar,
  size: state => state.app.size,
  device: state => state.app.device,
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  info: state => state.user.info,
  user_statistical: state => state.user.user_statistical,
  introduction: state => state.user.introduction,
  roles: state => state.user.roles,
  menuRoutes: state => state.permission.menuRoutes,
  permissions: state => state.user.permissions,
  is_dev: state => state.user.is_dev,
}
export default getters
