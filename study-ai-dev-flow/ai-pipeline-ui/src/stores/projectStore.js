import { reactive } from 'vue'

const state = reactive({
  currentProject: null,
  projects: []
})

const mutations = {
  setCurrentProject(project) {
    state.currentProject = project
  },
  setProjects(projects) {
    state.projects = projects
  },
  clearCurrentProject() {
    state.currentProject = null
  }
}

const getters = {
  getCurrentProject() {
    return state.currentProject
  },
  getProjects() {
    return state.projects
  },
  hasCurrentProject() {
    return !!state.currentProject
  }
}

export const projectStore = {
  state,
  ...mutations,
  ...getters
}
