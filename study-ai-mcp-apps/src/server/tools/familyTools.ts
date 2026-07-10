import { z } from 'zod';
import { createUIResource } from '@mcp-ui/server';
import { familyStore, User } from '../familyStore';
import { memberStore, Member } from '../memberStore';
import { relationshipStore, Relationship, RelationshipType } from '../relationshipStore';
import { historyStore } from '../historyStore';
import { albumStore } from '../albumStore';
import { feedStore } from '../feedStore';
import { permissionStore, PermissionLevel } from '../permissionStore';
import { memorialStore } from '../memorialStore';
import { statisticsStore } from '../statisticsStore';
import { notificationStore } from '../notificationStore';
import { generateFamilyManageHtml } from '../ui/familyManageHtml';
import { generateFamilyTreeHtml } from '../ui/familyTreeHtml';
import { generateMemberManageHtml } from '../ui/memberManageHtml';
import { generateRelationshipHtml } from '../ui/relationshipHtml';
import { generateHistoryHtml } from '../ui/historyHtml';
import { generateLoginHtml } from '../ui/loginHtml';
import { generateHomeHtml } from '../ui/homeHtml';
import { generateProfileHtml } from '../ui/profileHtml';
import { generateAlbumHtml } from '../ui/albumHtml';
import { generateFeedHtml } from '../ui/feedHtml';
import { generateMemorialHtml } from '../ui/memorialHtml';
import { generateStatsHtml } from '../ui/statsHtml';
import { generateMemberDetailHtml } from '../ui/memberDetailHtml';
import { getSearchHtml } from '../ui/searchHtml';

interface SessionData {
  user: User;
  expiresAt: number;
}

const userSessions = new Map<string, SessionData>();

let currentSessionId = 'default-session';

const SESSION_EXPIRY_MS = 24 * 60 * 60 * 1000;

export function setSessionId(sessionId: string) {
  currentSessionId = sessionId;
}

export function getCurrentUser(): User | null {
  const session = userSessions.get(currentSessionId);
  if (!session) {
    return null;
  }
  
  if (Date.now() > session.expiresAt) {
    userSessions.delete(currentSessionId);
    return null;
  }
  
  session.expiresAt = Date.now() + SESSION_EXPIRY_MS;
  return session.user;
}

export const loginTool = {
  name: 'login',
  options: {
    inputSchema: z.object({
      email: z.string().email().describe('用户邮箱'),
      password: z.string().describe('用户密码'),
    }),
    outputSchema: z.object({
      success: z.boolean(),
      message: z.string(),
      user: z.object({
        id: z.string(),
        email: z.string(),
        nickname: z.string(),
      }).optional(),
    }),
    title: '用户登录',
    description: '用户登录系统',
  },
  handler: async (args) => {
    const { email, password } = args;
    const user = familyStore.findUserByEmail(email);
    
    if (!user) {
      return { success: false, message: '邮箱或密码错误' };
    }
    
    const isPasswordValid = await familyStore.verifyPassword(user, password);
    if (!isPasswordValid) {
      return { success: false, message: '邮箱或密码错误' };
    }
    
    userSessions.set(currentSessionId, {
      user,
      expiresAt: Date.now() + SESSION_EXPIRY_MS,
    });
    return { 
      success: true, 
      message: '登录成功',
      user: { id: user.id, email: user.email, nickname: user.nickname }
    };
  },
};

export const registerTool = {
  name: 'register',
  options: {
    inputSchema: z.object({
      email: z.string().email().describe('用户邮箱'),
      password: z.string().describe('用户密码'),
      nickname: z.string().describe('用户昵称'),
    }),
    outputSchema: z.object({
      success: z.boolean(),
      message: z.string(),
    }),
    title: '用户注册',
    description: '用户注册新账号',
  },
  handler: async (args) => {
    const { email, password, nickname } = args;
    
    if (familyStore.findUserByEmail(email)) {
      return { success: false, message: '该邮箱已被注册' };
    }
    
    await familyStore.createUser(email, password, nickname);
    return { success: true, message: '注册成功，请登录' };
  },
};

export const getLoginUITool = {
  name: 'getLoginUI',
  options: {
    inputSchema: z.object({}),
    title: '获取登录UI',
    description: '获取登录页面',
  },
  handler: async () => {
    const html = generateLoginHtml();
    const uiResource = await createUIResource({
      uri: 'ui://family/login',
      content: { type: 'rawHtml', htmlString: html },
      encoding: 'text',
    });
    return { content: [uiResource] };
  },
};

export const getHomeUITool = {
  name: 'getHomeUI',
  options: {
    inputSchema: z.object({}),
    title: '获取首页UI',
    description: '获取首页',
  },
  handler: async () => {
    const user = getCurrentUser();
    const html = generateHomeHtml(user);
    const uiResource = await createUIResource({
      uri: 'ui://family/home',
      content: { type: 'rawHtml', htmlString: html },
      encoding: 'text',
    });
    return { content: [uiResource] };
  },
};

export const getSearchUITool = {
  name: 'getSearchUI',
  options: {
    inputSchema: z.object({}),
    title: '获取全局搜索UI',
    description: '获取全局搜索页面',
  },
  handler: async () => {
    const html = getSearchHtml();
    const uiResource = await createUIResource({
      uri: 'ui://family/search',
      content: { type: 'rawHtml', htmlString: html },
      encoding: 'text',
    });
    return { content: [uiResource] };
  },
};

export const getFamilyManageUITool = {
  name: 'getFamilyManageUI',
  options: {
    inputSchema: z.object({
      familyId: z.string().optional().describe('家族ID'),
    }),
    title: '获取家族管理UI',
    description: '获取家族管理页面',
  },
  handler: async (args) => {
    const { familyId } = args;
    const user = getCurrentUser();
    const html = generateFamilyManageHtml(user, familyId);
    const uiResource = await createUIResource({
      uri: 'ui://family/manage',
      content: { type: 'rawHtml', htmlString: html },
      encoding: 'text',
    });
    return { content: [uiResource] };
  },
};

export const getFamilyTreeUITool = {
  name: 'getFamilyTreeUI',
  options: {
    inputSchema: z.object({
      familyId: z.string().optional().describe('家族ID'),
    }),
    title: '获取家族树UI',
    description: '获取家族树可视化页面',
  },
  handler: async (args) => {
    const { familyId } = args;
    const user = getCurrentUser();
    const html = generateFamilyTreeHtml(user, familyId);
    const uiResource = await createUIResource({
      uri: 'ui://family/tree',
      content: { type: 'rawHtml', htmlString: html },
      encoding: 'text',
    });
    return { content: [uiResource] };
  },
};

export const getMemberManageUITool = {
  name: 'getMemberManageUI',
  options: {
    inputSchema: z.object({
      familyId: z.string().optional().describe('家族ID'),
    }),
    title: '获取成员管理UI',
    description: '获取成员管理页面',
  },
  handler: async (args) => {
    const { familyId } = args;
    const user = getCurrentUser();
    const html = generateMemberManageHtml(user, familyId);
    const uiResource = await createUIResource({
      uri: 'ui://family/members',
      content: { type: 'rawHtml', htmlString: html },
      encoding: 'text',
    });
    return { content: [uiResource] };
  },
};

export const getMemberDetailUITool = {
  name: 'getMemberDetailUI',
  options: {
    inputSchema: z.object({
      memberId: z.string().describe('成员ID'),
    }),
    title: '获取成员详情UI',
    description: '获取成员详情页面',
  },
  handler: async (args) => {
    const { memberId } = args;
    const user = getCurrentUser();
    const html = generateMemberDetailHtml(user, memberId);
    const uiResource = await createUIResource({
      uri: 'ui://family/member-detail',
      content: { type: 'rawHtml', htmlString: html },
      encoding: 'text',
    });
    return { content: [uiResource] };
  },
};

export const getRelationshipUITool = {
  name: 'getRelationshipUI',
  options: {
    inputSchema: z.object({
      familyId: z.string().optional().describe('家族ID'),
    }),
    title: '获取关系管理UI',
    description: '获取关系管理页面',
  },
  handler: async (args) => {
    const { familyId } = args;
    const user = getCurrentUser();
    const html = generateRelationshipHtml(user, familyId);
    const uiResource = await createUIResource({
      uri: 'ui://family/relationships',
      content: { type: 'rawHtml', htmlString: html },
      encoding: 'text',
    });
    return { content: [uiResource] };
  },
};

export const getHistoryUITool = {
  name: 'getHistoryUI',
  options: {
    inputSchema: z.object({
      familyId: z.string().optional().describe('家族ID'),
    }),
    title: '获取历史记录UI',
    description: '获取历史记录页面',
  },
  handler: async (args) => {
    const { familyId } = args;
    const user = getCurrentUser();
    const html = generateHistoryHtml(user, familyId);
    const uiResource = await createUIResource({
      uri: 'ui://family/history',
      content: { type: 'rawHtml', htmlString: html },
      encoding: 'text',
    });
    return { content: [uiResource] };
  },
};

export const listFamiliesTool = {
  name: 'listFamilies',
  options: {
    inputSchema: z.object({}),
    outputSchema: z.object({
      families: z.array(z.object({
        id: z.string(),
        name: z.string(),
        description: z.string(),
        createdAt: z.string(),
        createdBy: z.string(),
      })),
    }),
    title: '获取家族列表',
    description: '获取所有家族列表',
  },
  handler: async () => {
    return { families: familyStore.getAllFamilies() };
  },
};

export const getFamilyByIdTool = {
  name: 'getFamilyById',
  options: {
    inputSchema: z.object({
      familyId: z.string().describe('家族ID'),
    }),
    outputSchema: z.object({
      family: z.object({
        id: z.string(),
        name: z.string(),
        description: z.string(),
        createdAt: z.string(),
        createdBy: z.string(),
      }).nullable(),
    }),
    title: '获取家族详情',
    description: '根据ID获取家族详情',
  },
  handler: async (args) => {
    const { familyId } = args;
    return { family: familyStore.getFamilyById(familyId) || null };
  },
};

export const createFamilyTool = {
  name: 'createFamily',
  options: {
    inputSchema: z.object({
      name: z.string().describe('家族名称'),
      description: z.string().describe('家族描述'),
    }),
    outputSchema: z.object({
      success: z.boolean(),
      message: z.string(),
      family: z.object({
        id: z.string(),
        name: z.string(),
        description: z.string(),
        createdAt: z.string(),
        createdBy: z.string(),
      }).optional(),
    }),
    title: '创建家族',
    description: '创建新家族',
  },
  handler: async (args) => {
    const { name, description } = args;
    const user = getCurrentUser();
    if (!user) {
      return { success: false, message: '请先登录' };
    }
    
    const family = familyStore.createFamily(name, description, user.id);
    return { success: true, message: '家族创建成功', family };
  },
};

export const updateFamilyTool = {
  name: 'updateFamily',
  options: {
    inputSchema: z.object({
      familyId: z.string().describe('家族ID'),
      name: z.string().describe('家族名称'),
      description: z.string().describe('家族描述'),
    }),
    outputSchema: z.object({
      success: z.boolean(),
      message: z.string(),
    }),
    title: '更新家族',
    description: '更新家族信息',
  },
  handler: async (args) => {
    const { familyId, name, description } = args;
    const user = getCurrentUser();
    if (!user) {
      return { success: false, message: '请先登录' };
    }
    
    const family = familyStore.getFamilyById(familyId);
    if (!family) {
      return { success: false, message: '家族不存在' };
    }
    
    if (family.adminId !== user.id) {
      return { success: false, message: '无权限修改' };
    }
    
    familyStore.updateFamily(familyId, name, description);
    return { success: true, message: '家族更新成功' };
  },
};

export const deleteFamilyTool = {
  name: 'deleteFamily',
  options: {
    inputSchema: z.object({
      familyId: z.string().describe('家族ID'),
    }),
    outputSchema: z.object({
      success: z.boolean(),
      message: z.string(),
    }),
    title: '删除家族',
    description: '删除家族',
  },
  handler: async (args) => {
    const { familyId } = args;
    const user = getCurrentUser();
    if (!user) {
      return { success: false, message: '请先登录' };
    }
    
    const family = familyStore.getFamilyById(familyId);
    if (!family) {
      return { success: false, message: '家族不存在' };
    }
    
    if (family.adminId !== user.id) {
      return { success: false, message: '无权限删除' };
    }
    
    familyStore.deleteFamily(familyId);
    return { success: true, message: '家族删除成功' };
  },
};

export const listMembersTool = {
  name: 'listMembers',
  options: {
    inputSchema: z.object({
      familyId: z.string().optional().describe('家族ID'),
    }),
    outputSchema: z.object({
      members: z.array(z.object({
        id: z.string(),
        familyId: z.string(),
        name: z.string(),
        gender: z.string(),
        birthDate: z.string().nullable(),
        deathDate: z.string().nullable(),
        phone: z.string().nullable(),
        email: z.string().nullable(),
        details: z.string().nullable(),
      })),
    }),
    title: '获取成员列表',
    description: '获取家族成员列表',
  },
  handler: async (args) => {
    const { familyId } = args;
    let members = memberStore.getAllMembers();
    if (familyId) {
      members = members.filter(m => m.familyId === familyId);
    }
    return { members };
  },
};

export const getMemberByIdTool = {
  name: 'getMemberById',
  options: {
    inputSchema: z.object({
      memberId: z.string().describe('成员ID'),
    }),
    outputSchema: z.object({
      member: z.object({
        id: z.string(),
        familyId: z.string(),
        name: z.string(),
        gender: z.string(),
        birthDate: z.string().nullable(),
        deathDate: z.string().nullable(),
        phone: z.string().nullable(),
        email: z.string().nullable(),
        details: z.string().nullable(),
      }).nullable(),
    }),
    title: '获取成员详情',
    description: '根据ID获取成员详情',
  },
  handler: async (args) => {
    const { memberId } = args;
    return { member: memberStore.getMemberById(memberId) || null };
  },
};

export const createMemberTool = {
  name: 'createMember',
  options: {
    inputSchema: z.object({
      familyId: z.string().describe('家族ID'),
      name: z.string().describe('成员姓名'),
      gender: z.enum(['male', 'female']).describe('性别'),
      birthDate: z.string().optional().describe('出生日期'),
      deathDate: z.string().optional().describe('去世日期'),
      phone: z.string().optional().describe('手机号'),
      email: z.string().optional().describe('邮箱'),
      details: z.string().optional().describe('详细信息'),
    }),
    outputSchema: z.object({
      success: z.boolean(),
      message: z.string(),
      member: z.object({
        id: z.string(),
        familyId: z.string(),
        name: z.string(),
      }).optional(),
    }),
    title: '创建成员',
    description: '创建新成员',
  },
  handler: async (args) => {
    const { familyId, name, gender, birthDate, deathDate, phone, email, details } = args;
    const user = getCurrentUser();
    if (!user) {
      return { success: false, message: '请先登录' };
    }
    
    const member = memberStore.createMember({
      familyId,
      name,
      gender,
      birthDate: birthDate || null,
      deathDate: deathDate || null,
      phone: phone || null,
      email: email || null,
      details: details || null,
      photo: null,
    });
    return { success: true, message: '成员创建成功', member: { id: member.id, familyId: member.familyId, name: member.name } };
  },
};

export const updateMemberTool = {
  name: 'updateMember',
  options: {
    inputSchema: z.object({
      memberId: z.string().describe('成员ID'),
      name: z.string().optional().describe('成员姓名'),
      gender: z.enum(['male', 'female']).optional().describe('性别'),
      birthDate: z.string().optional().describe('出生日期'),
      deathDate: z.string().optional().describe('去世日期'),
      phone: z.string().optional().describe('手机号'),
      email: z.string().optional().describe('邮箱'),
      details: z.string().optional().describe('详细信息'),
    }),
    outputSchema: z.object({
      success: z.boolean(),
      message: z.string(),
    }),
    title: '更新成员',
    description: '更新成员信息',
  },
  handler: async (args) => {
    const { memberId, name, gender, birthDate, deathDate, phone, email, details } = args;
    const user = getCurrentUser();
    if (!user) {
      return { success: false, message: '请先登录' };
    }
    
    const updateData: Partial<Member> = {};
    if (name !== undefined) updateData.name = name;
    if (gender !== undefined) updateData.gender = gender;
    if (birthDate !== undefined) updateData.birthDate = birthDate || null;
    if (deathDate !== undefined) updateData.deathDate = deathDate || null;
    if (phone !== undefined) updateData.phone = phone || null;
    if (email !== undefined) updateData.email = email || null;
    if (details !== undefined) updateData.details = details || null;
    
    const member = memberStore.updateMember(memberId, updateData);
    if (!member) {
      return { success: false, message: '成员不存在' };
    }
    
    return { success: true, message: '成员更新成功' };
  },
};

export const deleteMemberTool = {
  name: 'deleteMember',
  options: {
    inputSchema: z.object({
      memberId: z.string().describe('成员ID'),
    }),
    outputSchema: z.object({
      success: z.boolean(),
      message: z.string(),
    }),
    title: '删除成员',
    description: '删除成员',
  },
  handler: async (args) => {
    const { memberId } = args;
    const user = getCurrentUser();
    if (!user) {
      return { success: false, message: '请先登录' };
    }
    
    const success = memberStore.deleteMember(memberId);
    if (!success) {
      return { success: false, message: '成员不存在' };
    }
    
    relationshipStore.deleteRelationshipsByMember(memberId);
    return { success: true, message: '成员删除成功' };
  },
};

export const searchMembersTool = {
  name: 'searchMembers',
  options: {
    inputSchema: z.object({
      keyword: z.string().describe('搜索关键词'),
      familyId: z.string().optional().describe('家族ID'),
    }),
    outputSchema: z.object({
      members: z.array(z.object({
        id: z.string(),
        name: z.string(),
        phone: z.string().nullable(),
        email: z.string().nullable(),
      })),
    }),
    title: '搜索成员',
    description: '搜索家族成员',
  },
  handler: async (args) => {
    const { keyword, familyId } = args;
    const members = memberStore.searchMembers(keyword, familyId);
    return { members: members.map(m => ({ id: m.id, name: m.name, phone: m.phone, email: m.email })) };
  },
};

export const listRelationshipsTool = {
  name: 'listRelationships',
  options: {
    inputSchema: z.object({
      familyId: z.string().optional().describe('家族ID'),
    }),
    outputSchema: z.object({
      relationships: z.array(z.object({
        id: z.string(),
        memberId1: z.string(),
        memberId2: z.string(),
        relationshipType: z.string(),
      })),
    }),
    title: '获取关系列表',
    description: '获取家族关系列表',
  },
  handler: async (args) => {
    const { familyId } = args;
    let relationships = relationshipStore.getAllRelationships();
    if (familyId) {
      const members = memberStore.getMembersByFamily(familyId);
      relationships = relationshipStore.getRelationshipsByFamily(familyId, members);
    }
    return { relationships };
  },
};

export const createRelationshipTool = {
  name: 'createRelationship',
  options: {
    inputSchema: z.object({
      memberId1: z.string().describe('成员1ID'),
      memberId2: z.string().describe('成员2ID'),
      relationshipType: z.enum(['father', 'mother', 'husband', 'wife', 'son', 'daughter', 'brother', 'sister', 'grandfather', 'grandmother', 'grandson', 'granddaughter', 'uncle', 'aunt', 'nephew', 'niece', 'cousin']).describe('关系类型'),
    }),
    outputSchema: z.object({
      success: z.boolean(),
      message: z.string(),
    }),
    title: '创建关系',
    description: '创建成员关系',
  },
  handler: async (args) => {
    const { memberId1, memberId2, relationshipType } = args;
    const user = getCurrentUser();
    if (!user) {
      return { success: false, message: '请先登录' };
    }
    
    const member1 = memberStore.getMemberById(memberId1);
    const member2 = memberStore.getMemberById(memberId2);
    
    if (!member1 || !member2) {
      return { success: false, message: '成员不存在' };
    }
    
    const validation = relationshipStore.validateRelationship(
      memberId1,
      memberId2,
      relationshipType as RelationshipType,
      member1.gender,
      member2.gender
    );
    
    if (!validation.valid) {
      return { success: false, message: validation.message };
    }
    
    relationshipStore.createRelationship(memberId1, memberId2, relationshipType as RelationshipType);
    return { success: true, message: '关系创建成功' };
  },
};

export const deleteRelationshipTool = {
  name: 'deleteRelationship',
  options: {
    inputSchema: z.object({
      relationshipId: z.string().describe('关系ID'),
    }),
    outputSchema: z.object({
      success: z.boolean(),
      message: z.string(),
    }),
    title: '删除关系',
    description: '删除成员关系',
  },
  handler: async (args) => {
    const { relationshipId } = args;
    const user = getCurrentUser();
    if (!user) {
      return { success: false, message: '请先登录' };
    }
    
    const success = relationshipStore.deleteRelationship(relationshipId);
    if (!success) {
      return { success: false, message: '关系不存在' };
    }
    
    return { success: true, message: '关系删除成功' };
  },
};

export const getRelationshipLabelTool = {
  name: 'getRelationshipLabel',
  options: {
    inputSchema: z.object({
      type: z.string().describe('关系类型'),
    }),
    outputSchema: z.object({
      label: z.string(),
    }),
    title: '获取关系标签',
    description: '获取关系类型的中文标签',
  },
  handler: async (args) => {
    const { type } = args;
    return { label: relationshipStore.getRelationshipLabel(type as RelationshipType) };
  },
};

export const listEventsTool = {
  name: 'listEvents',
  options: {
    inputSchema: z.object({
      familyId: z.string().describe('家族ID'),
    }),
    outputSchema: z.object({
      events: z.array(z.object({
        id: z.string(),
        type: z.string(),
        title: z.string(),
        description: z.string(),
        date: z.string(),
        relatedMemberId: z.string().nullable(),
        relatedMemberName: z.string().nullable(),
        operator: z.string().nullable(),
      })),
    }),
    title: '获取历史事件列表',
    description: '获取家族历史事件列表',
  },
  handler: async (args) => {
    const { familyId } = args;
    const events = historyStore.getEventsByFamily(familyId);
    const result = events.map(event => {
      const relatedMember = event.relatedMemberId ? memberStore.getMemberById(event.relatedMemberId) : null;
      return {
        ...event,
        relatedMemberName: relatedMember?.name || null,
      };
    });
    return { events: result };
  },
};

export const createEventTool = {
  name: 'createEvent',
  options: {
    inputSchema: z.object({
      familyId: z.string().describe('家族ID'),
      type: z.enum(['event', 'milestone', 'log']).describe('事件类型'),
      title: z.string().describe('事件标题'),
      description: z.string().describe('事件描述'),
      date: z.string().describe('事件日期'),
      relatedMemberId: z.string().optional().describe('相关成员ID'),
    }),
    outputSchema: z.object({
      success: z.boolean(),
      message: z.string(),
    }),
    title: '创建历史事件',
    description: '创建家族历史事件',
  },
  handler: async (args) => {
    const { familyId, type, title, description, date, relatedMemberId } = args;
    const user = getCurrentUser();
    if (!user) {
      return { success: false, message: '请先登录' };
    }
    
    historyStore.createEvent({
      familyId,
      type,
      title,
      description,
      date,
      relatedMemberId: relatedMemberId || null,
      operator: user.nickname,
    });
    return { success: true, message: '事件创建成功' };
  },
};

export const deleteEventTool = {
  name: 'deleteEvent',
  options: {
    inputSchema: z.object({
      eventId: z.string().describe('事件ID'),
    }),
    outputSchema: z.object({
      success: z.boolean(),
      message: z.string(),
    }),
    title: '删除历史事件',
    description: '删除家族历史事件',
  },
  handler: async (args) => {
    const { eventId } = args;
    const user = getCurrentUser();
    if (!user) {
      return { success: false, message: '请先登录' };
    }
    
    const success = historyStore.deleteEvent(eventId);
    if (!success) {
      return { success: false, message: '事件不存在' };
    }
    
    return { success: true, message: '事件删除成功' };
  },
};

export const getProfileUITool = {
  name: 'getProfileUI',
  options: {
    inputSchema: z.object({}),
    title: '获取个人中心UI',
    description: '获取个人中心页面',
  },
  handler: async () => {
    const user = getCurrentUser();
    const html = generateProfileHtml(user);
    const uiResource = await createUIResource({
      uri: 'ui://family/profile',
      content: { type: 'rawHtml', htmlString: html },
      encoding: 'text',
    });
    return { content: [uiResource] };
  },
};

export const getAlbumUITool = {
  name: 'getAlbumUI',
  options: {
    inputSchema: z.object({
      familyId: z.string().optional().describe('家族ID'),
      albumId: z.string().optional().describe('相册ID'),
    }),
    title: '获取相册UI',
    description: '获取相册页面',
  },
  handler: async (args) => {
    const { familyId, albumId } = args;
    const user = getCurrentUser();
    const html = generateAlbumHtml(user, familyId, albumId);
    const uiResource = await createUIResource({
      uri: 'ui://family/album',
      content: { type: 'rawHtml', htmlString: html },
      encoding: 'text',
    });
    return { content: [uiResource] };
  },
};

export const getFeedUITool = {
  name: 'getFeedUI',
  options: {
    inputSchema: z.object({
      familyId: z.string().optional().describe('家族ID'),
    }),
    title: '获取家族动态UI',
    description: '获取家族动态页面',
  },
  handler: async (args) => {
    const { familyId } = args;
    const user = getCurrentUser();
    const html = generateFeedHtml(user, familyId);
    const uiResource = await createUIResource({
      uri: 'ui://family/feed',
      content: { type: 'rawHtml', htmlString: html },
      encoding: 'text',
    });
    return { content: [uiResource] };
  },
};

export const getMemorialUITool = {
  name: 'getMemorialUI',
  options: {
    inputSchema: z.object({
      familyId: z.string().optional().describe('家族ID'),
    }),
    title: '获取纪念堂UI',
    description: '获取纪念堂页面',
  },
  handler: async (args) => {
    const { familyId } = args;
    const user = getCurrentUser();
    const html = generateMemorialHtml(user, familyId);
    const uiResource = await createUIResource({
      uri: 'ui://family/memorial',
      content: { type: 'rawHtml', htmlString: html },
      encoding: 'text',
    });
    return { content: [uiResource] };
  },
};

export const getStatsUITool = {
  name: 'getStatsUI',
  options: {
    inputSchema: z.object({
      familyId: z.string().optional().describe('家族ID'),
    }),
    title: '获取统计分析UI',
    description: '获取统计分析页面',
  },
  handler: async (args) => {
    const { familyId } = args;
    const user = getCurrentUser();
    const html = generateStatsHtml(user, familyId);
    const uiResource = await createUIResource({
      uri: 'ui://family/stats',
      content: { type: 'rawHtml', htmlString: html },
      encoding: 'text',
    });
    return { content: [uiResource] };
  },
};

export const updateProfileTool = {
  name: 'updateProfile',
  options: {
    inputSchema: z.object({
      nickname: z.string().optional().describe('用户昵称'),
      phone: z.string().optional().describe('手机号'),
      avatar: z.string().optional().describe('头像URL'),
    }),
    outputSchema: z.object({
      success: z.boolean(),
      message: z.string(),
    }),
    title: '更新用户资料',
    description: '更新用户个人资料',
  },
  handler: async (args) => {
    const { nickname, phone, avatar } = args;
    const user = getCurrentUser();
    if (!user) {
      return { success: false, message: '请先登录' };
    }

    const updateData: Partial<User> = {};
    if (nickname !== undefined) updateData.nickname = nickname;
    if (phone !== undefined) updateData.phone = phone;
    if (avatar !== undefined) updateData.avatar = avatar;

    familyStore.updateUser(user.id, updateData);
    return { success: true, message: '资料更新成功' };
  },
};

export const changePasswordTool = {
  name: 'changePassword',
  options: {
    inputSchema: z.object({
      oldPassword: z.string().describe('旧密码'),
      newPassword: z.string().describe('新密码'),
    }),
    outputSchema: z.object({
      success: z.boolean(),
      message: z.string(),
    }),
    title: '修改密码',
    description: '修改用户密码',
  },
  handler: async (args) => {
    const { oldPassword, newPassword } = args;
    const user = getCurrentUser();
    if (!user) {
      return { success: false, message: '请先登录' };
    }

    const success = await familyStore.updatePassword(user.id, oldPassword, newPassword);
    if (!success) {
      return { success: false, message: '旧密码错误' };
    }

    return { success: true, message: '密码修改成功' };
  },
};

export const getFamilyMembersTool = {
  name: 'getFamilyMembers',
  options: {
    inputSchema: z.object({
      familyId: z.string().describe('家族ID'),
    }),
    outputSchema: z.object({
      members: z.array(z.object({
        id: z.string(),
        userId: z.string(),
        userName: z.string(),
        permission: z.string(),
        joinedAt: z.string(),
      })),
    }),
    title: '获取家族成员列表',
    description: '获取家族成员及其权限列表',
  },
  handler: async (args) => {
    const { familyId } = args;
    const members = permissionStore.getFamilyMembers(familyId);
    return { members: members.map(m => ({
      id: m.id,
      userId: m.userId,
      userName: m.userName,
      permission: m.permission,
      joinedAt: m.joinedAt,
    })) };
  },
};

export const updatePermissionTool = {
  name: 'updatePermission',
  options: {
    inputSchema: z.object({
      familyId: z.string().describe('家族ID'),
      userId: z.string().describe('用户ID'),
      permission: z.enum(['admin', 'editor', 'viewer']).describe('权限级别'),
    }),
    outputSchema: z.object({
      success: z.boolean(),
      message: z.string(),
    }),
    title: '更新成员权限',
    description: '更新家族成员的权限',
  },
  handler: async (args) => {
    const { familyId, userId, permission } = args;
    const user = getCurrentUser();
    if (!user) {
      return { success: false, message: '请先登录' };
    }

    if (!permissionStore.hasPermission(familyId, user.id, 'admin')) {
      return { success: false, message: '无权限操作' };
    }

    const updated = permissionStore.updatePermission(familyId, userId, permission);
    if (!updated) {
      return { success: false, message: '成员不存在' };
    }

    return { success: true, message: '权限更新成功' };
  },
};

export const inviteMemberTool = {
  name: 'inviteMember',
  options: {
    inputSchema: z.object({
      familyId: z.string().describe('家族ID'),
      email: z.string().email().describe('邀请邮箱'),
    }),
    outputSchema: z.object({
      success: z.boolean(),
      message: z.string(),
      invitation: z.object({
        id: z.string(),
        email: z.string(),
        token: z.string(),
      }).optional(),
    }),
    title: '邀请家人',
    description: '邀请家人加入家族',
  },
  handler: async (args) => {
    const { familyId, email } = args;
    const user = getCurrentUser();
    if (!user) {
      return { success: false, message: '请先登录' };
    }

    if (!permissionStore.hasPermission(familyId, user.id, 'admin')) {
      return { success: false, message: '无权限邀请' };
    }

    const invitation = permissionStore.createInvitation(familyId, email, user.id);
    return { success: true, message: '邀请已发送', invitation: {
      id: invitation.id,
      email: invitation.email,
      token: invitation.token,
    } };
  },
};

export const listInvitationsTool = {
  name: 'listInvitations',
  options: {
    inputSchema: z.object({
      familyId: z.string().describe('家族ID'),
    }),
    outputSchema: z.object({
      invitations: z.array(z.object({
        id: z.string(),
        email: z.string(),
        status: z.string(),
        createdAt: z.string(),
        expiresAt: z.string(),
      })),
    }),
    title: '获取邀请列表',
    description: '获取家族邀请列表',
  },
  handler: async (args) => {
    const { familyId } = args;
    const invitations = permissionStore.getInvitationsByFamily(familyId);
    return { invitations: invitations.map(i => ({
      id: i.id,
      email: i.email,
      status: i.status,
      createdAt: i.createdAt,
      expiresAt: i.expiresAt,
    })) };
  },
};

export const listAlbumsTool = {
  name: 'listAlbums',
  options: {
    inputSchema: z.object({
      familyId: z.string().describe('家族ID'),
    }),
    outputSchema: z.object({
      albums: z.array(z.object({
        id: z.string(),
        name: z.string(),
        description: z.string(),
        photoCount: z.number(),
        coverPhotoUrl: z.string().nullable(),
        createdAt: z.string(),
      })),
    }),
    title: '获取相册列表',
    description: '获取家族相册列表',
  },
  handler: async (args) => {
    const { familyId } = args;
    const albums = albumStore.getAlbumsByFamily(familyId);
    const result = albums.map(album => {
      const photos = albumStore.getPhotosByAlbum(album.id);
      const coverPhoto = album.coverPhotoId ? albumStore.getPhotoById(album.coverPhotoId) : null;
      return {
        id: album.id,
        name: album.name,
        description: album.description,
        photoCount: photos.length,
        coverPhotoUrl: coverPhoto?.thumbnailUrl || null,
        createdAt: album.createdAt,
      };
    });
    return { albums: result };
  },
};

export const createAlbumTool = {
  name: 'createAlbum',
  options: {
    inputSchema: z.object({
      familyId: z.string().describe('家族ID'),
      name: z.string().describe('相册名称'),
      description: z.string().describe('相册描述'),
    }),
    outputSchema: z.object({
      success: z.boolean(),
      message: z.string(),
      album: z.object({
        id: z.string(),
        name: z.string(),
      }).optional(),
    }),
    title: '创建相册',
    description: '创建新相册',
  },
  handler: async (args) => {
    const { familyId, name, description } = args;
    const user = getCurrentUser();
    if (!user) {
      return { success: false, message: '请先登录' };
    }

    const album = albumStore.createAlbum(familyId, name, description, user.id);
    return { success: true, message: '相册创建成功', album: { id: album.id, name: album.name } };
  },
};

export const deleteAlbumTool = {
  name: 'deleteAlbum',
  options: {
    inputSchema: z.object({
      albumId: z.string().describe('相册ID'),
    }),
    outputSchema: z.object({
      success: z.boolean(),
      message: z.string(),
    }),
    title: '删除相册',
    description: '删除相册',
  },
  handler: async (args) => {
    const { albumId } = args;
    const user = getCurrentUser();
    if (!user) {
      return { success: false, message: '请先登录' };
    }

    const success = albumStore.deleteAlbum(albumId);
    if (!success) {
      return { success: false, message: '相册不存在' };
    }

    return { success: true, message: '相册删除成功' };
  },
};

export const listPhotosTool = {
  name: 'listPhotos',
  options: {
    inputSchema: z.object({
      albumId: z.string().describe('相册ID'),
    }),
    outputSchema: z.object({
      photos: z.array(z.object({
        id: z.string(),
        title: z.string(),
        description: z.string(),
        url: z.string(),
        thumbnailUrl: z.string(),
        uploadedAt: z.string(),
        relatedMemberName: z.string().nullable(),
      })),
    }),
    title: '获取照片列表',
    description: '获取相册照片列表',
  },
  handler: async (args) => {
    const { albumId } = args;
    const photos = albumStore.getPhotosByAlbum(albumId);
    const result = photos.map(photo => {
      const relatedMember = photo.relatedMemberId ? memberStore.getMemberById(photo.relatedMemberId) : null;
      return {
        id: photo.id,
        title: photo.title,
        description: photo.description,
        url: photo.url,
        thumbnailUrl: photo.thumbnailUrl,
        uploadedAt: photo.uploadedAt,
        relatedMemberName: relatedMember?.name || null,
      };
    });
    return { photos: result };
  },
};

export const createPhotoTool = {
  name: 'createPhoto',
  options: {
    inputSchema: z.object({
      familyId: z.string().describe('家族ID'),
      albumId: z.string().describe('相册ID'),
      title: z.string().describe('照片标题'),
      description: z.string().describe('照片描述'),
      url: z.string().describe('照片URL'),
      thumbnailUrl: z.string().describe('缩略图URL'),
      relatedMemberId: z.string().optional().describe('相关成员ID'),
    }),
    outputSchema: z.object({
      success: z.boolean(),
      message: z.string(),
      photo: z.object({
        id: z.string(),
        title: z.string(),
      }).optional(),
    }),
    title: '上传照片',
    description: '上传照片到相册',
  },
  handler: async (args) => {
    const { familyId, albumId, title, description, url, thumbnailUrl, relatedMemberId } = args;
    const user = getCurrentUser();
    if (!user) {
      return { success: false, message: '请先登录' };
    }

    const photo = albumStore.createPhoto({
      familyId,
      albumId,
      title,
      description,
      url,
      thumbnailUrl,
      uploadedBy: user.id,
      relatedMemberId: relatedMemberId || null,
    });
    return { success: true, message: '照片上传成功', photo: { id: photo.id, title: photo.title } };
  },
};

export const deletePhotoTool = {
  name: 'deletePhoto',
  options: {
    inputSchema: z.object({
      photoId: z.string().describe('照片ID'),
    }),
    outputSchema: z.object({
      success: z.boolean(),
      message: z.string(),
    }),
    title: '删除照片',
    description: '删除照片',
  },
  handler: async (args) => {
    const { photoId } = args;
    const user = getCurrentUser();
    if (!user) {
      return { success: false, message: '请先登录' };
    }

    const success = albumStore.deletePhoto(photoId);
    if (!success) {
      return { success: false, message: '照片不存在' };
    }

    return { success: true, message: '照片删除成功' };
  },
};

export const listFeedsTool = {
  name: 'listFeeds',
  options: {
    inputSchema: z.object({
      familyId: z.string().describe('家族ID'),
    }),
    outputSchema: z.object({
      feeds: z.array(z.object({
        id: z.string(),
        userId: z.string(),
        userName: z.string(),
        type: z.string(),
        content: z.string(),
        photoUrl: z.string().nullable(),
        createdAt: z.string(),
        likes: z.number(),
        comments: z.array(z.object({
          id: z.string(),
          userName: z.string(),
          content: z.string(),
          createdAt: z.string(),
        })),
      })),
    }),
    title: '获取家族动态',
    description: '获取家族动态列表',
  },
  handler: async (args) => {
    const { familyId } = args;
    const feeds = feedStore.getFeedsByFamily(familyId);
    const result = feeds.map(feed => ({
      id: feed.id,
      userId: feed.userId,
      userName: feed.userName,
      type: feed.type,
      content: feed.content,
      photoUrl: feed.photoUrl,
      createdAt: feed.createdAt,
      likes: feed.likes,
      comments: feed.comments.map(c => ({
        id: c.id,
        userName: c.userName,
        content: c.content,
        createdAt: c.createdAt,
      })),
    }));
    return { feeds: result };
  },
};

export const createFeedTool = {
  name: 'createFeed',
  options: {
    inputSchema: z.object({
      familyId: z.string().describe('家族ID'),
      type: z.enum(['text', 'photo', 'event', 'announcement']).describe('动态类型'),
      content: z.string().describe('动态内容'),
      photoUrl: z.string().optional().describe('照片URL'),
      relatedMemberId: z.string().optional().describe('相关成员ID'),
    }),
    outputSchema: z.object({
      success: z.boolean(),
      message: z.string(),
    }),
    title: '发布动态',
    description: '发布家族动态',
  },
  handler: async (args) => {
    const { familyId, type, content, photoUrl, relatedMemberId } = args;
    const user = getCurrentUser();
    if (!user) {
      return { success: false, message: '请先登录' };
    }

    feedStore.createFeed({
      familyId,
      userId: user.id,
      userName: user.nickname,
      type,
      content,
      photoUrl: photoUrl || null,
      relatedMemberId: relatedMemberId || null,
    });
    return { success: true, message: '动态发布成功' };
  },
};

export const deleteFeedTool = {
  name: 'deleteFeed',
  options: {
    inputSchema: z.object({
      feedId: z.string().describe('动态ID'),
    }),
    outputSchema: z.object({
      success: z.boolean(),
      message: z.string(),
    }),
    title: '删除动态',
    description: '删除家族动态',
  },
  handler: async (args) => {
    const { feedId } = args;
    const user = getCurrentUser();
    if (!user) {
      return { success: false, message: '请先登录' };
    }

    const success = feedStore.deleteFeed(feedId);
    if (!success) {
      return { success: false, message: '动态不存在' };
    }

    return { success: true, message: '动态删除成功' };
  },
};

export const toggleLikeTool = {
  name: 'toggleLike',
  options: {
    inputSchema: z.object({
      feedId: z.string().describe('动态ID'),
    }),
    outputSchema: z.object({
      success: z.boolean(),
      likes: z.number(),
    }),
    title: '点赞/取消点赞',
    description: '对动态进行点赞或取消点赞',
  },
  handler: async (args) => {
    const { feedId } = args;
    const user = getCurrentUser();
    if (!user) {
      return { success: false, likes: 0 };
    }

    const feed = feedStore.toggleLike(feedId, user.id);
    if (!feed) {
      return { success: false, likes: 0 };
    }

    return { success: true, likes: feed.likes };
  },
};

export const addCommentTool = {
  name: 'addComment',
  options: {
    inputSchema: z.object({
      feedId: z.string().describe('动态ID'),
      content: z.string().describe('评论内容'),
    }),
    outputSchema: z.object({
      success: z.boolean(),
      message: z.string(),
    }),
    title: '添加评论',
    description: '对动态添加评论',
  },
  handler: async (args) => {
    const { feedId, content } = args;
    const user = getCurrentUser();
    if (!user) {
      return { success: false, message: '请先登录' };
    }

    const feed = feedStore.addComment(feedId, {
      userId: user.id,
      userName: user.nickname,
      content,
    });
    if (!feed) {
      return { success: false, message: '动态不存在' };
    }

    return { success: true, message: '评论成功' };
  },
};

export const listMemorialsTool = {
  name: 'listMemorials',
  options: {
    inputSchema: z.object({
      familyId: z.string().describe('家族ID'),
    }),
    outputSchema: z.object({
      memorials: z.array(z.object({
        id: z.string(),
        memberId: z.string(),
        memberName: z.string(),
        birthDate: z.string(),
        deathDate: z.string(),
        epitaph: z.string().nullable(),
        portraitUrl: z.string().nullable(),
      })),
    }),
    title: '获取纪念堂列表',
    description: '获取纪念堂列表',
  },
  handler: async (args) => {
    const { familyId } = args;
    const memorials = memorialStore.getMemorialsByFamily(familyId);
    return { memorials };
  },
};

export const createMemorialTool = {
  name: 'createMemorial',
  options: {
    inputSchema: z.object({
      familyId: z.string().describe('家族ID'),
      memberId: z.string().describe('成员ID'),
      memberName: z.string().describe('成员姓名'),
      birthDate: z.string().describe('出生日期'),
      deathDate: z.string().describe('去世日期'),
      epitaph: z.string().optional().describe('墓志铭'),
      portraitUrl: z.string().optional().describe('肖像URL'),
      obituary: z.string().optional().describe('生平简介'),
    }),
    outputSchema: z.object({
      success: z.boolean(),
      message: z.string(),
    }),
    title: '创建纪念堂',
    description: '为逝者创建纪念堂',
  },
  handler: async (args) => {
    const { familyId, memberId, memberName, birthDate, deathDate, epitaph, portraitUrl, obituary } = args;
    const user = getCurrentUser();
    if (!user) {
      return { success: false, message: '请先登录' };
    }

    memorialStore.createMemorial({
      familyId,
      memberId,
      memberName,
      birthDate,
      deathDate,
      epitaph: epitaph || null,
      portraitUrl: portraitUrl || null,
      obituary: obituary || null,
      createdBy: user.id,
    });
    return { success: true, message: '纪念堂创建成功' };
  },
};

export const getStatisticsTool = {
  name: 'getStatistics',
  options: {
    inputSchema: z.object({
      familyId: z.string().optional().describe('家族ID，为空时返回所有家族汇总'),
    }),
    outputSchema: z.object({
      statistics: z.object({
        totalMembers: z.number(),
        maleCount: z.number(),
        femaleCount: z.number(),
        averageAge: z.number(),
        generationCount: z.number(),
        aliveCount: z.number(),
        deceasedCount: z.number(),
        ageDistribution: z.record(z.number()),
        genderRatio: z.object({
          male: z.number(),
          female: z.number(),
          ratio: z.number(),
        }),
      }),
    }),
    title: '获取统计数据',
    description: '获取家族统计数据',
  },
  handler: async (args) => {
    const { familyId } = args;
    const families = familyStore.getAllFamilies();
    const members = memberStore.getAllMembers();
    
    let filteredMembers = members;
    let generationCount = 4;

    if (familyId) {
      const family = families.find(f => f.id === familyId);
      if (!family) {
        return { statistics: {
          totalMembers: 0,
          maleCount: 0,
          femaleCount: 0,
          averageAge: 0,
          generationCount: 0,
          aliveCount: 0,
          deceasedCount: 0,
          ageDistribution: {},
          genderRatio: { male: 0, female: 0, ratio: 0 },
        } };
      }
      filteredMembers = members.filter(m => m.familyId === familyId);
    } else {
      const maxGeneration = Math.max(...families.map(f => statisticsStore.getFamilyStatistics(f.id, f.name, members).generationCount));
      generationCount = maxGeneration;
    }

    const totalMembers = filteredMembers.length;
    const maleCount = filteredMembers.filter(m => m.gender === 'male').length;
    const femaleCount = filteredMembers.filter(m => m.gender === 'female').length;
    
    const membersWithAge = filteredMembers.filter(m => m.birthDate).map(m => ({
      ...m,
      age: m.birthDate ? statisticsStore.calculateAge(m.birthDate, m.deathDate) : 0,
    }));
    
    const averageAge = membersWithAge.length > 0 
      ? Math.round(membersWithAge.reduce((sum, m) => sum + m.age, 0) / membersWithAge.length)
      : 0;
    
    const aliveCount = filteredMembers.filter(m => !m.deathDate).length;
    const deceasedCount = filteredMembers.filter(m => m.deathDate).length;

    const ageDistribution: Record<string, number> = {
      '0-18': 0,
      '19-30': 0,
      '31-50': 0,
      '51-70': 0,
      '71+': 0,
    };

    filteredMembers.filter(m => m.birthDate).forEach(m => {
      const age = m.birthDate ? statisticsStore.calculateAge(m.birthDate, m.deathDate) : 0;
      if (age <= 18) ageDistribution['0-18']++;
      else if (age <= 30) ageDistribution['19-30']++;
      else if (age <= 50) ageDistribution['31-50']++;
      else if (age <= 70) ageDistribution['51-70']++;
      else ageDistribution['71+']++;
    });

    const total = maleCount + femaleCount;
    const genderRatio = { 
      male: maleCount, 
      female: femaleCount, 
      ratio: total > 0 ? Math.round((maleCount / total) * 100) : 0 
    };

    return { statistics: {
      totalMembers,
      maleCount,
      femaleCount,
      averageAge,
      generationCount,
      aliveCount,
      deceasedCount,
      ageDistribution,
      genderRatio,
    } };
  },
};

export const exportDataTool = {
  name: 'exportData',
  options: {
    inputSchema: z.object({
      type: z.enum(['all', 'members', 'families', 'relationships', 'events', 'albums', 'feeds', 'memorials']).describe('导出类型'),
      familyId: z.string().optional().describe('家族ID，为空时导出所有家族'),
    }),
    outputSchema: z.object({
      success: z.boolean(),
      message: z.string(),
      data: z.record(z.any()).optional(),
      filename: z.string().optional(),
    }),
    title: '数据导出',
    description: '导出家族数据为JSON格式',
  },
  handler: async (args) => {
    const { type, familyId } = args;
    
    const exportData: Record<string, any> = {};
    
    switch (type) {
      case 'members': {
        const members = familyId 
          ? memberStore.getMembersByFamilyId(familyId)
          : memberStore.getAllMembers();
        exportData.members = members;
        break;
      }
      case 'families': {
        const families = familyId 
          ? [familyStore.getFamilyById(familyId)].filter(Boolean)
          : familyStore.getAllFamilies();
        exportData.families = families;
        break;
      }
      case 'relationships': {
        const allMembers = memberStore.getAllMembers();
        const relationships = familyId
          ? relationshipStore.getRelationshipsByFamily(familyId, allMembers)
          : relationshipStore.getAllRelationships();
        exportData.relationships = relationships;
        break;
      }
      case 'events': {
        const events = familyId
          ? historyStore.getEventsByFamily(familyId)
          : historyStore.getAllEvents();
        exportData.events = events;
        break;
      }
      case 'albums': {
        const albums = familyId
          ? albumStore.getAlbumsByFamilyId(familyId)
          : albumStore.getAllAlbums();
        exportData.albums = albums;
        const albumIds = albums.map(a => a.id);
        const photos = albumStore.getAllPhotos().filter(p => albumIds.includes(p.albumId));
        exportData.photos = photos;
        break;
      }
      case 'feeds': {
        const feeds = familyId
          ? feedStore.getFeedsByFamily(familyId)
          : feedStore.getAllFeeds();
        exportData.feeds = feeds;
        break;
      }
      case 'memorials': {
        const memorials = familyId
          ? memorialStore.getMemorialsByFamily(familyId)
          : memorialStore.getAllMemorials();
        exportData.memorials = memorials;
        break;
      }
      case 'all': {
        exportData.families = familyId
          ? [familyStore.getFamilyById(familyId)].filter(Boolean)
          : familyStore.getAllFamilies();
        exportData.members = familyId
          ? memberStore.getMembersByFamilyId(familyId)
          : memberStore.getAllMembers();
        const allMembers = memberStore.getAllMembers();
        exportData.relationships = familyId
          ? relationshipStore.getRelationshipsByFamily(familyId, allMembers)
          : relationshipStore.getAllRelationships();
        exportData.events = familyId
          ? historyStore.getEventsByFamily(familyId)
          : historyStore.getAllEvents();
        exportData.albums = familyId
          ? albumStore.getAlbumsByFamilyId(familyId)
          : albumStore.getAllAlbums();
        const albumIds = (exportData.albums as any[]).map(a => a.id);
        exportData.photos = albumStore.getAllPhotos().filter(p => albumIds.includes(p.albumId));
        exportData.feeds = familyId
          ? feedStore.getFeedsByFamily(familyId)
          : feedStore.getAllFeeds();
        exportData.memorials = familyId
          ? memorialStore.getMemorialsByFamily(familyId)
          : memorialStore.getAllMemorials();
        exportData.users = familyStore.getAllUsers();
        exportData.permissions = familyId
          ? permissionStore.getPermissionsByFamily(familyId)
          : permissionStore.getAllPermissions();
        break;
      }
    }
    
    const timestamp = new Date().toISOString().replace(/[:.]/g, '-');
    const filename = `family-data-${type}-${timestamp}.json`;
    
    return {
      success: true,
      message: '数据导出成功',
      data: exportData,
      filename,
    };
  },
};

export const getNotificationsTool = {
  name: 'getNotifications',
  options: {
    inputSchema: z.object({}),
    outputSchema: z.object({
      notifications: z.array(z.object({
        id: z.string(),
        type: z.enum(['invite', 'like', 'comment', 'event', 'reminder']),
        title: z.string(),
        message: z.string(),
        isRead: z.boolean(),
        createdAt: z.string(),
      })),
      unreadCount: z.number(),
    }),
    title: '获取通知',
    description: '获取当前用户的通知列表',
  },
  handler: async () => {
    const user = getCurrentUser();
    if (!user) {
      return { notifications: [], unreadCount: 0 };
    }
    
    const notifications = notificationStore.getNotificationsByUser(user.id);
    const unreadCount = notificationStore.getUnreadCount(user.id);
    
    return { notifications, unreadCount };
  },
};

export const markNotificationAsReadTool = {
  name: 'markNotificationAsRead',
  options: {
    inputSchema: z.object({
      notificationId: z.string().describe('通知ID'),
    }),
    outputSchema: z.object({
      success: z.boolean(),
      message: z.string(),
    }),
    title: '标记通知为已读',
    description: '标记指定通知为已读',
  },
  handler: async (args) => {
    const { notificationId } = args;
    const success = notificationStore.markAsRead(notificationId);
    return {
      success,
      message: success ? '标记成功' : '标记失败',
    };
  },
};

export const markAllNotificationsAsReadTool = {
  name: 'markAllNotificationsAsRead',
  options: {
    inputSchema: z.object({}),
    outputSchema: z.object({
      success: z.boolean(),
      message: z.string(),
    }),
    title: '全部标记已读',
    description: '将所有通知标记为已读',
  },
  handler: async () => {
    const user = getCurrentUser();
    if (!user) {
      return { success: false, message: '请先登录' };
    }
    
    notificationStore.markAllAsRead(user.id);
    return { success: true, message: '全部标记为已读' };
  },
};

export const globalSearchTool = {
  name: 'globalSearch',
  options: {
    inputSchema: z.object({
      query: z.string().describe('搜索关键词'),
      familyId: z.string().optional().describe('家族ID，为空时搜索所有家族'),
      type: z.enum(['all', 'member', 'family', 'album', 'feed', 'memorial']).optional().describe('搜索类型'),
    }),
    outputSchema: z.object({
      members: z.array(z.object({
        id: z.string(),
        name: z.string(),
        avatar: z.string().nullable(),
        role: z.string(),
        familyName: z.string(),
      })),
      families: z.array(z.object({
        id: z.string(),
        name: z.string(),
        description: z.string(),
        memberCount: z.number(),
      })),
      albums: z.array(z.object({
        id: z.string(),
        name: z.string(),
        description: z.string(),
        photoCount: z.number(),
        familyName: z.string(),
      })),
      feeds: z.array(z.object({
        id: z.string(),
        content: z.string(),
        createdAt: z.string(),
        authorName: z.string(),
        familyName: z.string(),
      })),
      memorials: z.array(z.object({
        id: z.string(),
        name: z.string(),
        title: z.string(),
        familyName: z.string(),
      })),
      totalResults: z.number(),
    }),
    title: '全局搜索',
    description: '搜索家族、成员、相册、动态和纪念堂',
  },
  handler: async (args) => {
    const { query, familyId, type = 'all' } = args;
    const lowerQuery = query.toLowerCase();
    
    const results = {
      members: [],
      families: [],
      albums: [],
      feeds: [],
      memorials: [],
      totalResults: 0,
    };
    
    if (type === 'all' || type === 'member') {
      const members = memberStore.getAllMembers();
      let filteredMembers = members;
      if (familyId) {
        filteredMembers = members.filter(m => m.familyId === familyId);
      }
      
      const matchedMembers = filteredMembers
        .filter(m => 
          m.name.toLowerCase().includes(lowerQuery) ||
          m.nickname?.toLowerCase().includes(lowerQuery) ||
          m.role?.toLowerCase().includes(lowerQuery)
        )
        .map(m => {
          const family = familyStore.getFamilyById(m.familyId);
          return {
            id: m.id,
            name: m.name,
            avatar: m.avatar,
            role: m.role,
            familyName: family?.name || '',
          };
        });
      
      results.members = matchedMembers;
    }
    
    if (type === 'all' || type === 'family') {
      const families = familyStore.getAllFamilies();
      let filteredFamilies = families;
      if (familyId) {
        filteredFamilies = families.filter(f => f.id === familyId);
      }
      
      const matchedFamilies = filteredFamilies
        .filter(f => 
          f.name.toLowerCase().includes(lowerQuery) ||
          f.description?.toLowerCase().includes(lowerQuery)
        )
        .map(f => {
          const members = memberStore.getMembersByFamilyId(f.id);
          return {
            id: f.id,
            name: f.name,
            description: f.description,
            memberCount: members.length,
          };
        });
      
      results.families = matchedFamilies;
    }
    
    if (type === 'all' || type === 'album') {
      const albums = albumStore.getAllAlbums();
      let filteredAlbums = albums;
      if (familyId) {
        filteredAlbums = albums.filter(a => a.familyId === familyId);
      }
      
      const matchedAlbums = filteredAlbums
        .filter(a => 
          a.name.toLowerCase().includes(lowerQuery) ||
          a.description?.toLowerCase().includes(lowerQuery)
        )
        .map(a => {
          const family = familyStore.getFamilyById(a.familyId);
          const photos = albumStore.getPhotosByAlbumId(a.id);
          return {
            id: a.id,
            name: a.name,
            description: a.description,
            photoCount: photos.length,
            familyName: family?.name || '',
          };
        });
      
      results.albums = matchedAlbums;
    }
    
    if (type === 'all' || type === 'feed') {
      const feeds = feedStore.getAllFeeds();
      let filteredFeeds = feeds;
      if (familyId) {
        filteredFeeds = feeds.filter(f => f.familyId === familyId);
      }
      
      const matchedFeeds = filteredFeeds
        .filter(f => 
          f.content.toLowerCase().includes(lowerQuery)
        )
        .map(f => {
          const family = familyStore.getFamilyById(f.familyId);
          const author = memberStore.getMemberById(f.authorId);
          return {
            id: f.id,
            content: f.content,
            createdAt: f.createdAt,
            authorName: author?.name || '',
            familyName: family?.name || '',
          };
        });
      
      results.feeds = matchedFeeds;
    }
    
    if (type === 'all' || type === 'memorial') {
      const memorials = memorialStore.getAllMemorials();
      let filteredMemorials = memorials;
      if (familyId) {
        filteredMemorials = memorials.filter(m => m.familyId === familyId);
      }
      
      const matchedMemorials = filteredMemorials
        .filter(m => 
          m.name.toLowerCase().includes(lowerQuery) ||
          m.title?.toLowerCase().includes(lowerQuery) ||
          m.description?.toLowerCase().includes(lowerQuery)
        )
        .map(m => {
          const family = familyStore.getFamilyById(m.familyId);
          return {
            id: m.id,
            name: m.name,
            title: m.title,
            familyName: family?.name || '',
          };
        });
      
      results.memorials = matchedMemorials;
    }
    
    results.totalResults = 
      results.members.length + 
      results.families.length + 
      results.albums.length + 
      results.feeds.length + 
      results.memorials.length;
    
    return results;
  },
};

export const getUpcomingRemindersTool = {
  name: 'getUpcomingReminders',
  options: {
    inputSchema: z.object({
      familyId: z.string().optional().describe('家族ID，为空时返回所有家族'),
      days: z.number().optional().describe('查询天数，默认7天'),
    }),
    outputSchema: z.object({
      birthdays: z.array(z.object({
        memberId: z.string(),
        name: z.string(),
        birthDate: z.string(),
        daysUntil: z.number(),
        age: z.number(),
      })),
      anniversaries: z.array(z.object({
        title: z.string(),
        date: z.string(),
        daysUntil: z.number(),
        relatedMemberId: z.string().nullable(),
        relatedMemberName: z.string().nullable(),
      })),
      totalBirthdaysThisMonth: z.number(),
      totalAnniversariesThisMonth: z.number(),
    }),
    title: '获取即将到来的提醒',
    description: '获取即将到来的生日和纪念日提醒',
  },
  handler: async (args) => {
    const { familyId, days = 7 } = args;
    const members = memberStore.getAllMembers();
    const events = historyStore.getAllEvents();
    
    let filteredMembers = members;
    let filteredEvents = events;
    
    if (familyId) {
      filteredMembers = members.filter(m => m.familyId === familyId);
      filteredEvents = events.filter(e => e.familyId === familyId);
    }
    
    const today = new Date();
    const todayMonth = today.getMonth();
    const todayDay = today.getDate();
    
    const birthdays = filteredMembers
      .filter(m => m.birthDate && !m.deathDate)
      .map(m => {
        const birthDate = new Date(m.birthDate!);
        let nextBirthday = new Date(today.getFullYear(), birthDate.getMonth(), birthDate.getDate());
        
        if (nextBirthday < today) {
          nextBirthday = new Date(today.getFullYear() + 1, birthDate.getMonth(), birthDate.getDate());
        }
        
        const diffTime = nextBirthday.getTime() - today.getTime();
        const daysUntil = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
        
        const age = today.getFullYear() - birthDate.getFullYear();
        
        return {
          memberId: m.id,
          name: m.name,
          birthDate: m.birthDate!,
          daysUntil,
          age,
        };
      })
      .filter(b => b.daysUntil <= days)
      .sort((a, b) => a.daysUntil - b.daysUntil);
    
    const anniversaries = filteredEvents
      .filter(e => e.type === 'milestone' || e.type === 'event')
      .map(e => {
        const eventDate = new Date(e.date);
        let nextAnniversary = new Date(today.getFullYear(), eventDate.getMonth(), eventDate.getDate());
        
        if (nextAnniversary < today) {
          nextAnniversary = new Date(today.getFullYear() + 1, eventDate.getMonth(), eventDate.getDate());
        }
        
        const diffTime = nextAnniversary.getTime() - today.getTime();
        const daysUntil = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
        
        const relatedMember = e.relatedMemberId ? memberStore.getMemberById(e.relatedMemberId) : null;
        
        return {
          title: e.title,
          date: e.date,
          daysUntil,
          relatedMemberId: e.relatedMemberId,
          relatedMemberName: relatedMember?.name || null,
        };
      })
      .filter(a => a.daysUntil <= days)
      .sort((a, b) => a.daysUntil - b.daysUntil);
    
    const totalBirthdaysThisMonth = filteredMembers.filter(m => {
      if (!m.birthDate || m.deathDate) return false;
      return new Date(m.birthDate).getMonth() === todayMonth;
    }).length;
    
    const totalAnniversariesThisMonth = filteredEvents.filter(e => {
      if (e.type !== 'milestone' && e.type !== 'event') return false;
      return new Date(e.date).getMonth() === todayMonth;
    }).length;
    
    return {
      birthdays,
      anniversaries,
      totalBirthdaysThisMonth,
      totalAnniversariesThisMonth,
    };
  },
};

export const getRelationLabelTool = {
  name: 'getRelationLabel',
  options: {
    inputSchema: z.object({
      memberId1: z.string().describe('成员1ID'),
      memberId2: z.string().describe('成员2ID'),
    }),
    outputSchema: z.object({
      label: z.string(),
    }),
    title: '获取关系称谓',
    description: '根据两个成员的ID和性别计算关系称谓',
  },
  handler: async (args) => {
    const { memberId1, memberId2 } = args;
    const member1 = memberStore.getMemberById(memberId1);
    const member2 = memberStore.getMemberById(memberId2);
    
    if (!member1 || !member2) {
      return { label: '未知关系' };
    }

    const relation = relationshipStore.calculateRelation(memberId1, memberId2, member1.gender, member2.gender);
    return { label: relation?.label || '未知关系' };
  },
};

export const familyTools = [
  loginTool,
  registerTool,
  getLoginUITool,
  getHomeUITool,
  getSearchUITool,
  getFamilyManageUITool,
  exportDataTool,
  getNotificationsTool,
  markNotificationAsReadTool,
  markAllNotificationsAsReadTool,
  getFamilyTreeUITool,
  getMemberManageUITool,
  getMemberDetailUITool,
  getRelationshipUITool,
  getHistoryUITool,
  getProfileUITool,
  getAlbumUITool,
  getFeedUITool,
  getMemorialUITool,
  getStatsUITool,
  listFamiliesTool,
  getFamilyByIdTool,
  createFamilyTool,
  updateFamilyTool,
  deleteFamilyTool,
  listMembersTool,
  getMemberByIdTool,
  createMemberTool,
  updateMemberTool,
  deleteMemberTool,
  searchMembersTool,
  globalSearchTool,
  listRelationshipsTool,
  createRelationshipTool,
  deleteRelationshipTool,
  getRelationshipLabelTool,
  getRelationLabelTool,
  listEventsTool,
  createEventTool,
  deleteEventTool,
  updateProfileTool,
  changePasswordTool,
  getFamilyMembersTool,
  updatePermissionTool,
  inviteMemberTool,
  listInvitationsTool,
  listAlbumsTool,
  createAlbumTool,
  deleteAlbumTool,
  listPhotosTool,
  createPhotoTool,
  deletePhotoTool,
  listFeedsTool,
  createFeedTool,
  deleteFeedTool,
  toggleLikeTool,
  addCommentTool,
  listMemorialsTool,
  createMemorialTool,
  getStatisticsTool,
  getUpcomingRemindersTool,
];
