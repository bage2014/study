import { z } from 'zod';
import { createUIResource } from '@mcp-ui/server';
import { familyStore, User } from '../familyStore';
import { memberStore, Member } from '../memberStore';
import { relationshipStore, Relationship, RelationshipType } from '../relationshipStore';
import { historyStore } from '../historyStore';
import { generateFamilyManageHtml } from '../ui/familyManageHtml';
import { generateFamilyTreeHtml } from '../ui/familyTreeHtml';
import { generateMemberManageHtml } from '../ui/memberManageHtml';
import { generateRelationshipHtml } from '../ui/relationshipHtml';
import { generateHistoryHtml } from '../ui/historyHtml';
import { generateLoginHtml } from '../ui/loginHtml';
import { generateHomeHtml } from '../ui/homeHtml';

const userSessions = new Map<string, User>();

let currentSessionId = 'default-session';

export function setSessionId(sessionId: string) {
  currentSessionId = sessionId;
}

export function getCurrentUser(): User | null {
  return userSessions.get(currentSessionId) || null;
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
    
    if (!user || user.password !== password) {
      return { success: false, message: '邮箱或密码错误' };
    }
    
    userSessions.set(currentSessionId, user);
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
    
    familyStore.createUser(email, password, nickname);
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

export const familyTools = [
  loginTool,
  registerTool,
  getLoginUITool,
  getHomeUITool,
  getFamilyManageUITool,
  getFamilyTreeUITool,
  getMemberManageUITool,
  getRelationshipUITool,
  getHistoryUITool,
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
  listRelationshipsTool,
  createRelationshipTool,
  deleteRelationshipTool,
  getRelationshipLabelTool,
  listEventsTool,
  createEventTool,
  deleteEventTool,
];
