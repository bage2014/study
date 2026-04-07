// 进度管理工具

const PROGRESS_FILE_PATH = '/progress.json';

/**
 * 读取项目进度
 * @returns {Promise<Object>} 进度对象
 */
export const readProgress = async () => {
  try {
    const response = await fetch(PROGRESS_FILE_PATH);
    if (!response.ok) {
      throw new Error('Failed to fetch progress file');
    }
    const progress = await response.json();
    return progress;
  } catch (error) {
    console.error('Error reading progress:', error);
    return null;
  }
};

/**
 * 更新项目进度
 * @param {Object} progress 进度对象
 * @returns {Promise<boolean>} 是否更新成功
 */
export const updateProgress = async (progress) => {
  try {
    // 这里只是模拟更新，实际项目中可能需要通过API更新
    console.log('Updating progress:', progress);
    // 在实际项目中，这里应该发送请求到后端API来更新进度
    // const response = await fetch('/api/progress', {
    //   method: 'POST',
    //   headers: {
    //     'Content-Type': 'application/json'
    //   },
    //   body: JSON.stringify(progress)
    // });
    // return response.ok;
    return true;
  } catch (error) {
    console.error('Error updating progress:', error);
    return false;
  }
};

/**
 * 添加已完成功能
 * @param {Object} feature 功能对象
 * @returns {Promise<boolean>} 是否添加成功
 */
export const addCompletedFeature = async (feature) => {
  try {
    const progress = await readProgress();
    if (!progress) {
      return false;
    }

    // 添加新的已完成功能
    progress.completedFeatures.push({
      ...feature,
      completedAt: new Date().toISOString()
    });

    // 更新整体进度
    progress.overallProgress = Math.round(
      (progress.completedFeatures.length / (progress.completedFeatures.length + progress.pendingTasks.length)) * 100
    );

    // 更新最后更新时间
    progress.lastUpdated = new Date().toISOString();

    // 保存更新后的进度
    return await updateProgress(progress);
  } catch (error) {
    console.error('Error adding completed feature:', error);
    return false;
  }
};

/**
 * 添加待完成任务
 * @param {Object} task 任务对象
 * @returns {Promise<boolean>} 是否添加成功
 */
export const addPendingTask = async (task) => {
  try {
    const progress = await readProgress();
    if (!progress) {
      return false;
    }

    // 添加新的待完成任务
    progress.pendingTasks.push(task);

    // 更新整体进度
    progress.overallProgress = Math.round(
      (progress.completedFeatures.length / (progress.completedFeatures.length + progress.pendingTasks.length)) * 100
    );

    // 更新最后更新时间
    progress.lastUpdated = new Date().toISOString();

    // 保存更新后的进度
    return await updateProgress(progress);
  } catch (error) {
    console.error('Error adding pending task:', error);
    return false;
  }
};

/**
 * 标记任务为已完成
 * @param {string} taskId 任务ID
 * @returns {Promise<boolean>} 是否标记成功
 */
export const markTaskCompleted = async (taskId) => {
  try {
    const progress = await readProgress();
    if (!progress) {
      return false;
    }

    // 找到待完成任务
    const taskIndex = progress.pendingTasks.findIndex(task => task.id === taskId);
    if (taskIndex === -1) {
      return false;
    }

    // 移除待完成任务
    const task = progress.pendingTasks.splice(taskIndex, 1)[0];

    // 添加到已完成功能
    progress.completedFeatures.push({
      id: task.id,
      name: task.name,
      description: task.description,
      completedAt: new Date().toISOString()
    });

    // 更新整体进度
    progress.overallProgress = Math.round(
      (progress.completedFeatures.length / (progress.completedFeatures.length + progress.pendingTasks.length)) * 100
    );

    // 更新最后更新时间
    progress.lastUpdated = new Date().toISOString();

    // 保存更新后的进度
    return await updateProgress(progress);
  } catch (error) {
    console.error('Error marking task as completed:', error);
    return false;
  }
};