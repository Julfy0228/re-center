export function canManageContent(user) {
  return user?.role === "ADMIN" || user?.role === "MANAGER";
}
