export function canManageContent(user) {
  return user?.role === "ADMIN" || user?.role === "MANAGER";
}

export function isAdmin(user) {
  return user?.role === "ADMIN";
}
