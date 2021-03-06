/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.geode.management.internal.web.controllers;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import org.apache.geode.internal.lang.StringUtils;
import org.apache.geode.management.internal.cli.i18n.CliStrings;
import org.apache.geode.management.internal.cli.util.CommandStringBuilder;
import org.apache.geode.management.internal.web.util.ConvertUtils;

/**
 * The DeployCommandsController class implements the GemFire Management REST API web service
 * endpoints for the Gfsh Deploy Commands.
 * <p/>
 *
 * @see org.apache.geode.management.internal.cli.commands.DeployCommand
 * @see org.apache.geode.management.internal.cli.commands.UndeployCommand
 * @see org.apache.geode.management.internal.cli.commands.ListDeployedCommand
 * @see org.apache.geode.management.internal.web.controllers.AbstractMultiPartCommandsController
 * @see org.springframework.stereotype.Controller
 * @see org.springframework.web.bind.annotation.RequestMapping
 * @see org.springframework.web.bind.annotation.RequestMethod
 * @see org.springframework.web.bind.annotation.RequestParam
 * @see org.springframework.web.bind.annotation.ResponseBody
 * @since GemFire 8.0
 */
@Controller("deployController")
@RequestMapping(AbstractCommandsController.REST_API_VERSION)
@SuppressWarnings("unused")
public class DeployCommandsController extends AbstractMultiPartCommandsController {

  @RequestMapping(method = RequestMethod.GET, value = "/deployed")
  @ResponseBody
  public String listDeployed(
      @RequestParam(value = CliStrings.GROUP, required = false) final String[] groups) {
    final CommandStringBuilder command = new CommandStringBuilder(CliStrings.LIST_DEPLOYED);

    if (hasValue(groups)) {
      command.addOption(CliStrings.GROUP, StringUtils.join(groups, StringUtils.COMMA_DELIMITER));
    }

    return processCommand(command.toString());
  }

  @RequestMapping(method = RequestMethod.POST, value = "/deployed")
  @ResponseBody
  public String deploy(
      @RequestParam(RESOURCES_REQUEST_PARAMETER) final MultipartFile[] jarFileResources,
      @RequestParam(value = CliStrings.GROUP, required = false) final String[] groups,
      @RequestParam(value = CliStrings.JAR, required = false) final String jarFileName,
      @RequestParam(value = CliStrings.DEPLOY__DIR, required = false) final String directory)
      throws IOException {
    final CommandStringBuilder command = new CommandStringBuilder(CliStrings.DEPLOY);

    if (hasValue(groups)) {
      command.addOption(CliStrings.GROUP, StringUtils.join(groups, StringUtils.COMMA_DELIMITER));
    }

    if (hasValue(jarFileName)) {
      command.addOption(CliStrings.JAR, jarFileName);
    }

    if (hasValue(directory)) {
      command.addOption(CliStrings.DEPLOY__DIR, directory);
    }
    return processCommand(command.toString(), ConvertUtils.convert(jarFileResources));
  }

  @RequestMapping(method = RequestMethod.DELETE, value = "/deployed")
  @ResponseBody
  public String undeploy(
      @RequestParam(value = CliStrings.GROUP, required = false) final String[] groups,
      @RequestParam(value = CliStrings.JAR, required = false) final String[] jarFileNames) {
    final CommandStringBuilder command = new CommandStringBuilder(CliStrings.UNDEPLOY);

    if (hasValue(groups)) {
      command.addOption(CliStrings.GROUP, StringUtils.join(groups, StringUtils.COMMA_DELIMITER));
    }

    if (hasValue(jarFileNames)) {
      command.addOption(CliStrings.JAR,
          StringUtils.join(jarFileNames, StringUtils.COMMA_DELIMITER));
    }
    return processCommand(command.toString());
  }
}
