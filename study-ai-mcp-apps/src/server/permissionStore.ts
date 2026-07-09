import { persistenceService } from './persistenceService';

export type PermissionLevel = 'admin' | 'editor' | 'viewer';

export interface FamilyMember {
  id: string;
  familyId: string;
  userId: string;
  userName: string;
  permission: PermissionLevel;
  joinedAt: string;
  invitedBy: string | null;
}

export interface Invitation {
  id: string;
  familyId: string;
  email: string;
  token: string;
  invitedBy: string;
  createdAt: string;
  expiresAt: string;
  status: 'pending' | 'accepted' | 'expired';
}

class PermissionStore {
  private familyMembers: FamilyMember[] = [];
  private invitations: Invitation[] = [];

  constructor() {
    const savedPermissions = persistenceService.getPermissions();
    const savedInvitations = persistenceService.getInvitations();
    
    if (savedPermissions.length > 0) {
      this.familyMembers = savedPermissions;
      this.invitations = savedInvitations;
    } else {
      this.familyMembers.push({
      id: 'fm-1',
      familyId: 'family-1',
      userId: 'user-1',
      userName: '管理员',
      permission: 'admin',
      joinedAt: '2024-01-01',
      invitedBy: null,
    });

    this.familyMembers.push({
      id: 'fm-2',
      familyId: 'family-1',
      userId: 'user-2',
      userName: '王芳',
      permission: 'editor',
      joinedAt: '2024-01-05',
      invitedBy: 'user-1',
    });

    this.familyMembers.push({
      id: 'fm-3',
      familyId: 'family-2',
      userId: 'user-2',
      userName: '王家人',
      permission: 'admin',
      joinedAt: '2024-01-02',
      invitedBy: null,
    });

    this.familyMembers.push({
      id: 'fm-4',
      familyId: 'family-3',
      userId: 'user-3',
      userName: '李家人',
      permission: 'admin',
      joinedAt: '2024-01-03',
      invitedBy: null,
    });

    this.invitations.push({
      id: 'invite-1',
      familyId: 'family-1',
      email: 'zhangwei@family.com',
      token: 'token-' + Date.now(),
      invitedBy: 'user-1',
      createdAt: new Date().toISOString().split('T')[0],
      expiresAt: new Date(Date.now() + 7 * 24 * 60 * 60 * 1000).toISOString().split('T')[0],
      status: 'pending',
    });

      persistenceService.setPermissions(this.familyMembers);
      persistenceService.setInvitations(this.invitations);
    }
  }

  getFamilyMembers(familyId: string): FamilyMember[] {
    return this.familyMembers.filter(fm => fm.familyId === familyId);
  }

  getFamilyMember(familyId: string, userId: string): FamilyMember | undefined {
    return this.familyMembers.find(fm => fm.familyId === familyId && fm.userId === userId);
  }

  addFamilyMember(familyId: string, userId: string, userName: string, permission: PermissionLevel, invitedBy: string | null): FamilyMember {
    const familyMember: FamilyMember = {
      id: 'fm-' + Date.now(),
      familyId,
      userId,
      userName,
      permission,
      joinedAt: new Date().toISOString().split('T')[0],
      invitedBy,
    };
    this.familyMembers.push(familyMember);
    persistenceService.setPermissions(this.familyMembers);
    return familyMember;
  }

  updatePermission(familyId: string, userId: string, permission: PermissionLevel): FamilyMember | undefined {
    const index = this.familyMembers.findIndex(fm => fm.familyId === familyId && fm.userId === userId);
    if (index === -1) return undefined;
    this.familyMembers[index].permission = permission;
    persistenceService.setPermissions(this.familyMembers);
    return this.familyMembers[index];
  }

  removeFamilyMember(familyId: string, userId: string): boolean {
    const index = this.familyMembers.findIndex(fm => fm.familyId === familyId && fm.userId === userId);
    if (index === -1) return false;
    this.familyMembers.splice(index, 1);
    persistenceService.setPermissions(this.familyMembers);
    return true;
  }

  hasPermission(familyId: string, userId: string, requiredPermission: PermissionLevel): boolean {
    const familyMember = this.getFamilyMember(familyId, userId);
    if (!familyMember) return false;

    const permissionOrder: Record<PermissionLevel, number> = {
      admin: 3,
      editor: 2,
      viewer: 1,
    };

    return permissionOrder[familyMember.permission] >= permissionOrder[requiredPermission];
  }

  createInvitation(familyId: string, email: string, invitedBy: string): Invitation {
    const invitation: Invitation = {
      id: 'invite-' + Date.now(),
      familyId,
      email,
      token: 'token-' + Date.now() + '-' + Math.random().toString(36).substr(2, 9),
      invitedBy,
      createdAt: new Date().toISOString().split('T')[0],
      expiresAt: new Date(Date.now() + 7 * 24 * 60 * 60 * 1000).toISOString().split('T')[0],
      status: 'pending',
    };
    this.invitations.push(invitation);
    persistenceService.setInvitations(this.invitations);
    return invitation;
  }

  getInvitationByToken(token: string): Invitation | undefined {
    return this.invitations.find(i => i.token === token && i.status === 'pending');
  }

  acceptInvitation(token: string): Invitation | undefined {
    const invitation = this.getInvitationByToken(token);
    if (!invitation) return undefined;

    if (new Date(invitation.expiresAt) < new Date()) {
      invitation.status = 'expired';
    } else {
      invitation.status = 'accepted';
    }
    persistenceService.setInvitations(this.invitations);
    return invitation;
  }

  getInvitationsByFamily(familyId: string): Invitation[] {
    return this.invitations.filter(i => i.familyId === familyId);
  }

  deleteInvitation(invitationId: string): boolean {
    const index = this.invitations.findIndex(i => i.id === invitationId);
    if (index === -1) return false;
    this.invitations.splice(index, 1);
    persistenceService.setInvitations(this.invitations);
    return true;
  }

  getPermissionLabel(permission: PermissionLevel): string {
    const labels: Record<PermissionLevel, string> = {
      admin: '管理员',
      editor: '编辑',
      viewer: '查看',
    };
    return labels[permission];
  }
}

export const permissionStore = new PermissionStore();
